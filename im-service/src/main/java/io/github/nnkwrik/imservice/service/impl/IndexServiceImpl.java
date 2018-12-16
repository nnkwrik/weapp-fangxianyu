package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.HistoryExample;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nnkwrik
 * @date 18/12/07 16:32
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private UserClientHandler userClientHandler;

    @Autowired
    private GoodsClientHandler goodsClientHandler;

    @Autowired
    private ChatMapper chatMapper;


    @Override
    public List<ChatIndex> showIndex(String currentUser, int size, Date offsetTime) {

        List<ChatIndex> resultVoList = new ArrayList<>();    //最终要返回的值
        Map<Integer, Integer> chatGoodsMap = new HashMap<>();         //k=chatId,v=需要去商品服务查的id
        Map<Integer, String> chatUserMap = new HashMap<>();           //k=chatId,v=需要去用户服务查的id

        List<Integer> chatIds = chatMapper.getChatIdsByUser(currentUser);
        List<List<WsMessage>> unreadMessage = redisClient.multiGet(
                chatIds.stream()
                        .map(id -> id + "")
                        .collect(Collectors.toList()));

        //从redis(未读)和sql(已读)中各尝试取10个
        Map<Integer, Integer> unreadCount = new HashMap<>();
        List<WsMessage> unread = getDisplayUnread(currentUser,unreadMessage, unreadCount, size, offsetTime);
        dealUnread(currentUser, unread, unreadCount, resultVoList, chatGoodsMap, chatUserMap);


        List<Integer> unreadChatIds = unreadMessage.stream()
                .filter(unreadList -> unreadList != null && unreadList.size() > 0)
                .map(unreadList -> unreadList.get((0)).getChatId())
                .collect(Collectors.toList());

        PageHelper.offsetPage(0, size);
        List<HistoryExample> read = historyMapper.getLastReadChat(unreadChatIds, currentUser, offsetTime);
        dealRead(read, currentUser, resultVoList, chatGoodsMap, chatUserMap);

        //排序后删除超出size的
        resultVoList = sortAndLimitMsg(size, resultVoList, chatGoodsMap, chatUserMap);


        //添加用户和商品信息,offsetTime
        resultVoList = setGoodsAndUser4Chat(resultVoList, chatGoodsMap, chatUserMap);


        return resultVoList;
    }


    private List<WsMessage> getDisplayUnread(String currentUserId,
                                             List<List<WsMessage>> unreadMessage,
                                             Map<Integer, Integer> unreadCount,
                                             int size, Date offsetTime) {
        if (ObjectUtils.isEmpty(unreadMessage)) return new ArrayList<>();
//        List<WsMessage> displayUnread = unreadMessage.stream()
//                .filter(msgList -> !ObjectUtils.isEmpty(msgList) && offsetTime.compareTo(msgList.get(msgList.size() - 1).getSendTime()) > 0)
//                .map(msgList -> {
//                    unreadCount.put(msgList.get(0).getChatId(), msgList.size());
//                    return msgList.get(msgList.size() - 1);
//                })
//                .sorted((a, b) -> b.getSendTime().compareTo(a.getSendTime()))
//                .limit(size)
//                .collect(Collectors.toList());

        List<WsMessage> displayUnread = unreadMessage.stream()
                .filter(msgList -> !ObjectUtils.isEmpty(msgList) && offsetTime.compareTo(msgList.get(msgList.size() - 1).getSendTime()) > 0)
                .map(msgList -> {
                    long count =  msgList.stream().filter(msg -> msg.getReceiverId().equals(currentUserId)).count();
                    unreadCount.put(msgList.get(0).getChatId(), Math.toIntExact(count));
                    return msgList.get(msgList.size() - 1);
                })
                .sorted((a, b) -> b.getSendTime().compareTo(a.getSendTime()))
                .limit(size)
                .collect(Collectors.toList());

        return displayUnread;

    }

    private void dealUnread(String currentUserId,
                            List<WsMessage> unread,
                            Map<Integer, Integer> unreadCount,
                            List<ChatIndex> resultVoList,
                            Map<Integer, Integer> chatGoodsMap,
                            Map<Integer, String> chatUserMap) {

        unread.stream().forEach(po -> {
            //稍后去其他服务查询
            chatGoodsMap.put(po.getChatId(), po.getGoodsId());


            //设置未读数,是自己发送的则不显示未读消息数
            ChatIndex vo = new ChatIndex();
            if (po.getSenderId().equals(currentUserId)) {
                chatUserMap.put(po.getChatId(), po.getReceiverId());
                vo.setUnreadCount(0);
            } else {
                vo.setUnreadCount(unreadCount.get(po.getChatId()));
                chatUserMap.put(po.getChatId(), po.getSenderId());
            }

            //设置最后一条信息
            History lastChat = new History();
            BeanUtils.copyProperties(po, lastChat);
            vo.setLastChat(lastChat);

            resultVoList.add(vo);
        });

    }

    private void dealRead(List<HistoryExample> read, String userId,
                          List<ChatIndex> resultVoList,
                          Map<Integer, Integer> chatGoodsMap,
                          Map<Integer, String> chatUserMap) {

        read.stream().forEach(po -> {

            chatGoodsMap.put(po.getChatId(), po.getGoodsId());
            if (userId.equals(po.getU1())) {
                chatUserMap.put(po.getChatId(), po.getU2());
            } else {
                chatUserMap.put(po.getChatId(), po.getU1());
            }

            //设置未读数
            ChatIndex vo = new ChatIndex();
            vo.setUnreadCount(0);

            //设置最后一条信息
            History lastChat = new History();
            BeanUtils.copyProperties(po, lastChat);
            vo.setLastChat(lastChat);

            resultVoList.add(vo);
        });

    }

    private List<ChatIndex> sortAndLimitMsg(int size,
                                            List<ChatIndex> voList,
                                            Map<Integer, Integer> chatGoodsMap,
                                            Map<Integer, String> chatUserMap) {
        List<ChatIndex> limited = voList.stream()
                .sorted((a, b) -> b.getLastChat().getSendTime().compareTo(a.getLastChat().getSendTime()))
                .limit(size)
                .collect(Collectors.toList());
        if (voList.size() > size) {
            Set<Integer> addedIds = new HashSet<>();
            addedIds.addAll(chatUserMap.keySet());
            addedIds.addAll(chatUserMap.keySet());
            List<Integer> needIds = voList.stream()
                    .map(vo -> vo.getLastChat().getChatId())
                    .collect(Collectors.toList());
            for (Integer added : addedIds) {

                if (!needIds.contains(added)) {
                    chatGoodsMap.remove(added);
                    chatUserMap.remove(added);
                }
            }
        }

        return limited;

    }

    private List<ChatIndex> setGoodsAndUser4Chat(List<ChatIndex> voList,
                                                 Map<Integer, Integer> chatGoodsMap,
                                                 Map<Integer, String> chatUserMap) {

        //去商品服务查商品图片
        Map<Integer, SimpleGoods> simpleGoodsMap
                = goodsClientHandler.getSimpleGoodsList(new ArrayList<>(chatGoodsMap.values()));

        //去用户服务查用户名字头像
        Map<String, SimpleUser> simpleUserMap
                = userClientHandler.getSimpleUserList(new ArrayList<>(chatUserMap.values()));


        voList.stream().forEach(vo -> {

            String userId = chatUserMap.get(vo.getLastChat().getChatId());

            SimpleUser simpleUser = simpleUserMap.get(userId);
            if (simpleUser == null) {
                simpleUser = SimpleUser.unknownUser();
            }
            vo.setOtherSide(simpleUser);

            Integer goodsId = chatGoodsMap.get(vo.getLastChat().getChatId());

            SimpleGoods simpleGoods = simpleGoodsMap.get(goodsId);
            if (simpleGoods == null) {
                simpleGoods = SimpleGoods.unknownGoods();
            }
            vo.setGoods(simpleGoods);

            vo.setOffsetTime(vo.getLastChat().getSendTime());

        });

        return voList;
    }

}

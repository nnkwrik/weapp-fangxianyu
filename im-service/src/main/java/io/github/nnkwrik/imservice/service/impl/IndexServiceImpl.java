package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.common.util.ListUtil;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.HistoryExample;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.model.vo.ChatIndexEle;
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
    public ChatIndex showIndex(String currentUser, int size, Date offsetTime) {

        //用户参与的所有chatId
        List<Integer> chatIds = chatMapper.getChatIdsByUser(currentUser);

        List<List<WsMessage>> unreadMessage = redisClient.multiGet(
                chatIds.stream()
                        .map(id -> id + "")
                        .collect(Collectors.toList()));

        //从redis(未读)和sql(已读)中各尝试取size个
        List<ChatIndexEle> unread = getDisplayUnread(currentUser, unreadMessage, size, offsetTime);

        List<Integer> unreadChatIds = unreadMessage.stream()
                .filter(unreadList -> unreadList != null && unreadList.size() > 0)
                .map(unreadList -> unreadList.get((0)).getChatId())
                .collect(Collectors.toList());

        List<ChatIndexEle> read = getDisplayRead(currentUser, unreadChatIds, size, offsetTime);

        //排序后删除超出size的
        List<ChatIndexEle> limited = sortAndLimit(unread, read, size);

        //添加用户和商品信息
        List<ChatIndexEle> chats = setGoodsAndUser(limited);

        ChatIndex vo = new ChatIndex();
        vo.setChats(chats);
        if (!ObjectUtils.isEmpty(chats)) {
            vo.setOffsetTime(ListUtil.getLast(chats).getLastChat().getSendTime());
        }

        return vo;
    }


    private List<ChatIndexEle> getDisplayUnread(String currentUserId,
                                                List<List<WsMessage>> unreadMessage,
                                                int size, Date offsetTime) {



        List<ChatIndexEle> unread = unreadMessage.stream()
                .filter(msgList -> !ObjectUtils.isEmpty(msgList) && offsetTime.compareTo(ListUtil.getLast(msgList).getSendTime()) > 0)
                .sorted((a, b) -> ListUtil.getLast(b).getSendTime().compareTo(ListUtil.getLast(a).getSendTime()))
                .limit(size)
                .map(msgList -> {

                    WsMessage lastMsg = ListUtil.getLast(msgList);

                    ChatIndexEle indexEle = new ChatIndexEle();
                    indexEle.setGoodsId(lastMsg.getGoodsId());
                    //设置未读数,是自己发送的则不显示未读消息数
                    if (lastMsg.getSenderId().equals(currentUserId)) {
                        indexEle.setUnreadCount(0);
                        indexEle.setUserId(lastMsg.getReceiverId());
                    } else {
                        long count = msgList.stream().filter(msg -> msg.getReceiverId().equals(currentUserId)).count();
                        indexEle.setUnreadCount(Math.toIntExact(count));
                        indexEle.setUserId(lastMsg.getSenderId());
                    }

                    //设置最后一条信息
                    History lastHistory = new History();
                    BeanUtils.copyProperties(lastMsg, lastHistory);
                    indexEle.setLastChat(lastHistory);

                    return indexEle;
                }).collect(Collectors.toList());

        return unread;

    }

    private List<ChatIndexEle> getDisplayRead(String currentUser,
                                              List<Integer> unreadChatIds,
                                              int size, Date offsetTime) {
        PageHelper.offsetPage(0, size);
        List<HistoryExample> readHistory = historyMapper.getLastReadChat(unreadChatIds, currentUser, offsetTime);

        List<ChatIndexEle> read = readHistory.stream()
                .map(history -> {
                    ChatIndexEle indexEle = new ChatIndexEle();
                    indexEle.setGoodsId(history.getGoodsId());
                    if (currentUser.equals(history.getU1())) {
                        indexEle.setUserId(history.getU2());
                    } else {
                        indexEle.setUserId(history.getU1());
                    }

                    //设置未读数
                    indexEle.setUnreadCount(0);

                    //设置最后一条信息
                    History lastChat = new History();
                    BeanUtils.copyProperties(history, lastChat);
                    indexEle.setLastChat(lastChat);

                    return indexEle;
                }).collect(Collectors.toList());

        return read;

    }


    private List<ChatIndexEle> sortAndLimit(List<ChatIndexEle> unread, List<ChatIndexEle> read, int size) {
        List<ChatIndexEle> display = new ArrayList<>();
        display.addAll(unread);
        display.addAll(read);

        List<ChatIndexEle> limited = display.stream()
                .sorted((a, b) -> b.getLastChat().getSendTime().compareTo(a.getLastChat().getSendTime()))
                .limit(size)
                .collect(Collectors.toList());
        return limited;

    }

    private List<ChatIndexEle> setGoodsAndUser(List<ChatIndexEle> eleList) {
        Set<Integer> goodsIds = new HashSet<>();
        Set<String> userIds = new HashSet<>();

        eleList.stream().forEach(ele -> {
            goodsIds.add(ele.getGoodsId());
            userIds.add(ele.getUserId());
        });

        //去商品服务查商品图片
        Map<Integer, SimpleGoods> simpleGoodsMap
                = goodsClientHandler.getSimpleGoodsList(new ArrayList<>(goodsIds));

        //去用户服务查用户名字头像
        Map<String, SimpleUser> simpleUserMap
                = userClientHandler.getSimpleUserList(new ArrayList<>(userIds));


        eleList.stream().forEach(ele -> {

            String userId = ele.getUserId();
            SimpleUser simpleUser = simpleUserMap.get(userId);
            if (simpleUser == null) {
                simpleUser = SimpleUser.unknownUser();
            } else {
                ele.setOtherSide(simpleUser);
            }

            Integer goodsId = ele.getGoodsId();
            SimpleGoods simpleGoods = simpleGoodsMap.get(goodsId);
            if (simpleGoods == null) {
                simpleGoods = SimpleGoods.unknownGoods();
            } else {
                ele.setGoods(simpleGoods);
            }
        });


        return eleList;
    }

}

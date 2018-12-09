package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.HistoryExample;
import io.github.nnkwrik.imservice.model.po.LastChat;
import io.github.nnkwrik.imservice.model.vo.ChatIndex;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    @Override
    public List<ChatIndex> showIndex(String currentUser, int page, int size) {
        List<LastChat> unreadMessage = redisClient.hvals(currentUser);

        List<ChatIndex> resultVoList = new ArrayList<>();    //最终要返回的值
        Map<Integer, Integer> chatGoodsMap = new HashMap<>();         //需要去商品服务查的id
        Map<Integer, String> chatUserMap = new HashMap<>();           //需要去用户服务查的id


        int alreadyShow = size * (page - 1);
        int needShow = page * size;
        if (unreadMessage.size() >= needShow) {
            //需要展示的内容全在redis中

            List<LastChat> unread = getDisplayUnread(unreadMessage, alreadyShow, size);

            dealUnread(unread, resultVoList, chatGoodsMap, chatUserMap);
        } else if (unreadMessage.size() > alreadyShow) {
            //需要的内容的一部分在redis(未读),一部分在sql(已读)

            //未读消息
            List<LastChat> unread = getDisplayUnread(unreadMessage, alreadyShow, size);
            dealUnread(unread, resultVoList, chatGoodsMap, chatUserMap);


            List<Integer> unreadChatIds = unread.stream()
                    .map(chat -> chat.getLastMsg().getChatId())
                    .collect(Collectors.toList());

            //已读消息
            int remain = needShow - unreadMessage.size();
            PageHelper.offsetPage(0, remain);
            List<HistoryExample> read = historyMapper.getLastReadChat(unreadChatIds);
            dealRead(read, currentUser, resultVoList, chatGoodsMap, chatUserMap);


        } else {
            //需要的内容全在sql

            int offset = alreadyShow - unreadMessage.size();
            PageHelper.offsetPage(offset, size);
            List<HistoryExample> read = historyMapper.getLastChat();
            dealRead(read, currentUser, resultVoList, chatGoodsMap, chatUserMap);
        }

        //添加用户和商品信息
        resultVoList = setGoodsAndUser4Chat(resultVoList, chatGoodsMap, chatUserMap);

        return resultVoList;
    }


    private List<LastChat> getDisplayUnread(List<LastChat> unreadMessage, int offset, int size) {
        List<LastChat> displayUnread = unreadMessage.stream()
                .sorted((a, b) -> b.getLastMsg().getSendTime().compareTo(a.getLastMsg().getSendTime()))
                .skip(offset)
                .limit(size)
                .collect(Collectors.toList());
        return displayUnread;
    }

    private void dealUnread(List<LastChat> unread,
                            List<ChatIndex> resultVoList,
                            Map<Integer, Integer> chatGoodsMap,
                            Map<Integer, String> chatUserMap) {

        unread.stream().forEach(po -> {
            //稍后去其他服务查询
            chatGoodsMap.put(po.getLastMsg().getChatId(), po.getLastMsg().getGoodsId());
            chatUserMap.put(po.getLastMsg().getChatId(), po.getLastMsg().getSenderId());

            //设置未读数
            ChatIndex vo = new ChatIndex();
            vo.setUnreadCount(po.getUnreadCount());

            //设置最后一条信息
            History lastChat = new History();
            BeanUtils.copyProperties(po.getLastMsg(), lastChat);
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

        });

        return voList;
    }

}

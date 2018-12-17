package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nnkwrik
 * @date 18/12/07 22:39
 */
@Service
@Slf4j
public class FormServiceImpl implements FormService {


    @Autowired
    private HistoryMapper historyMapper;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private UserClientHandler userClientHandler;

    @Autowired
    private GoodsClientHandler goodsClientHandler;

    @Autowired
    private RedisClient redisClient;


    @Override
    public ChatForm showForm(int chatId, String userId, int size, Date offsetTime) {


        ChatForm vo = new ChatForm();

        Chat chat = chatMapper.getChatById(chatId);
        SimpleGoods simpleGoods = goodsClientHandler.getSimpleGoods(chat.getGoodsId());
        vo.setGoods(simpleGoods);
        if (chat.getU1().equals(userId)) {
            SimpleUser simpleUser = userClientHandler.getSimpleUser(chat.getU2());
            vo.setOtherSide(simpleUser);
            vo.setIsU1(true);
        } else {
            SimpleUser simpleUser = userClientHandler.getSimpleUser(chat.getU1());
            vo.setOtherSide(simpleUser);
            vo.setIsU1(false);
        }


        //已读的消息
        PageHelper.offsetPage(0, size);
        List<History> chatHistory = historyMapper.getChatHistory(chatId, offsetTime);
        chatHistory = Lists.reverse(chatHistory);

        //自己或对方未读的消息,获取并把自己未读的设为已读
        List<History> unreadList = flushUnread(chatId, userId);

        if (!ObjectUtils.isEmpty(unreadList)) {
            chatHistory.addAll(unreadList);
            chatHistory = chatHistory.stream()
                    .filter(a -> offsetTime.compareTo(a.getSendTime()) > 0)
                    .sorted((a, b) -> b.getSendTime().compareTo(a.getSendTime()))
                    .limit(size)
                    .collect(Collectors.toList());
            chatHistory = Lists.reverse(chatHistory);
        }

        if (!ObjectUtils.isEmpty(chatHistory)) {
            vo.setHistoryList(chatHistory);
            vo.setOffsetTime(chatHistory.get(0).getSendTime());
        }


        return vo;
    }

    /**
     * 返回自己或对方未读的消息,获取并把自己未读的设为已读
     *
     * @param chatId
     * @param userId
     * @return
     */
    @Override
    public List<History> flushUnread(int chatId, String userId) {
        List<WsMessage> unreadMsgList = redisClient.get(chatId + "");
        if (!ObjectUtils.isEmpty(unreadMsgList)) {
            List<WsMessage> myUnreadMsgList = unreadMsgList.stream()
                    .filter(unread -> unread.getReceiverId().equals(userId))
                    .collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(myUnreadMsgList)) {
                log.info("把chatId={}设为已读消息", chatId);
                //添加为已读
                List<History> myUnreadHistory = WsListToHisList(myUnreadMsgList);
                historyMapper.addHistoryList(myUnreadHistory);

                //删除未读
                List<WsMessage> newUnreadList = new ArrayList<>();
                newUnreadList.addAll(unreadMsgList);
                newUnreadList.removeAll(myUnreadMsgList);
                redisClient.set(chatId + "", newUnreadList);
            }

            return WsListToHisList(unreadMsgList);
        } else {
            return null;
        }

    }


    private List<History> WsListToHisList(List<WsMessage> wsMessageList) {
        if (ObjectUtils.isEmpty(wsMessageList)) return null;
        String u1 = null;
        String u2 = null;
        WsMessage message = wsMessageList.get(0);
        if (message.getSenderId().compareTo(message.getReceiverId()) > 0) {
            u1 = message.getReceiverId();
            u2 = message.getSenderId();
        } else {
            u1 = message.getSenderId();
            u2 = message.getReceiverId();
        }
        boolean u1ToU2 = u1.equals(message.getSenderId()) ? true : false;

        List<History> historyList = new ArrayList<>();
        wsMessageList.stream().forEach(msg -> {
            History history = new History();
            BeanUtils.copyProperties(msg, history);
            history.setU1ToU2(u1ToU2);
            historyList.add(history);

        });

        return historyList;

    }


}

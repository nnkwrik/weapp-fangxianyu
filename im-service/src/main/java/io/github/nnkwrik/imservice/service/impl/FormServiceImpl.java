package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
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

        if (chat.getU1().equals(userId)) {
            vo.setOtherSide(userClientHandler.getSimpleUser(chat.getU2()));
            vo.setIsU1(true);
        } else {
            vo.setOtherSide(userClientHandler.getSimpleUser(chat.getU1()));
            vo.setIsU1(false);
        }
        vo.setGoods(goodsClientHandler.getSimpleGoods(chat.getGoodsId()));

        PageHelper.offsetPage(0, size);
        List<History> chatHistory = historyMapper.getChatHistory(chatId, offsetTime);
        chatHistory = Lists.reverse(chatHistory);

        List<History> unreadList = flushUnread(chatId, userId); //自己发送,但对方还未读的消息

        if (unreadList != null && unreadList.size() > 0) {
            chatHistory.addAll(unreadList);
            chatHistory = chatHistory.stream()
                    .filter(a -> offsetTime.compareTo(a.getSendTime()) > 1)
                    .sorted((a, b) -> a.getSendTime().compareTo(b.getSendTime()))
                    .limit(size)
                    .collect(Collectors.toList());
        }
        vo.setHistoryList(chatHistory);
        if (chatHistory.size() > 1) {
            vo.setOffsetTime(chatHistory.get(0).getSendTime());
        }

        return vo;
    }

    private List<History> flushUnread(int chatId, String userId) {
        List<WsMessage> unreadMsgList = redisClient.get(chatId + "");
        List<History> unreadHistory = WsListToHisList(unreadMsgList);
        if (unreadHistory != null && unreadHistory.size() > 0 && unreadMsgList.get(0).getReceiverId().equals(userId)) {
            log.info("把chatId={}设为已读消息", chatId);
            //添加聊天记录
            historyMapper.addHistoryList(unreadHistory);
            redisClient.del(chatId + "");
        }//否则不是receiver访问form,所以仍是未读状态,不刷入sql

        return unreadHistory;
    }

//    @Override
//    @Transactional
//    public void addMessageListToSQL(List<WsMessage> messageList) {
//
//
//        //添加聊天记录
//        historyMapper.addHistoryList(historyList);
//    }

    private List<History> WsListToHisList(List<WsMessage> wsMessageList) {
        if (wsMessageList == null || wsMessageList.size() < 1) return null;
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

package io.github.nnkwrik.imservice.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import fangxianyu.innerApi.goods.GoodsClientHandler;
import fangxianyu.innerApi.user.UserClientHandler;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.model.po.Chat;
import io.github.nnkwrik.imservice.model.po.History;
import io.github.nnkwrik.imservice.model.po.LastChat;
import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ChatForm showForm(int chatId, String userId, int page, int size, int offset) {
        LastChat lastChat = null;
        if (page == 1) {
            lastChat = flushUnread(chatId, userId);
        }

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

        int pageOffset = (page - 1) * size + offset;
        PageHelper.offsetPage(pageOffset, size);
        List<History> chatHistory = historyMapper.getChatHistory(chatId);
        chatHistory = Lists.reverse(chatHistory);

        if (lastChat != null) {
            History lastHistory = new History();
            BeanUtils.copyProperties(lastChat.getLastMsg(), lastHistory);
            if (lastChat.getLastMsg().getSenderId().compareTo(lastChat.getLastMsg().getReceiverId()) < 0) {
                lastHistory.setU1ToU2(true);
            }
            chatHistory.add(lastHistory);
        }

        vo.setHistoryList(chatHistory);

        return vo;
    }

    private LastChat flushUnread(int chatId, String userId) {
        LastChat lastChat = redisClient.get(chatId + "");
        if (lastChat != null && lastChat.getLastMsg().getReceiverId().equals(userId)) {
            log.info("把chatId={}设为已读消息", chatId);
            addMessageToSQL(lastChat.getLastMsg());
            redisClient.del(chatId + "");
            return null;
        }

        return lastChat;    //不是receiver访问form,所以仍是未读状态,不刷入sql
    }

//    public void flushWsMsgList(String userId, int chatId,List<WsMessage> wsMsgList){
//
//    }

    @Override
    @Transactional
    public void addMessageToSQL(WsMessage message) {
        String u1 = null;
        String u2 = null;
        if (message.getSenderId().compareTo(message.getReceiverId()) > 0) {
            u1 = message.getReceiverId();
            u2 = message.getSenderId();
        } else {
            u1 = message.getSenderId();
            u2 = message.getReceiverId();
        }


        //添加聊天记录
        History history = new History();
        BeanUtils.copyProperties(message, history);
        boolean u1ToU2 = u1.equals(message.getSendTime()) ? true : false;
        history.setU1ToU2(u1ToU2);
        historyMapper.addHistory(history);
    }


}

package io.github.nnkwrik.imservice.service.impl;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.exception.GlobalException;
import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.imservice.constant.MessageType;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.model.po.LastChat;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.WebSocketService;
import io.github.nnkwrik.imservice.websocket.ChatEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/12/05 12:30
 */
@Service
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private ChatEndpoint chatEndpoint;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private RedisClient redisClient;

    @Override
    public void OnMessage(String senderId, String rawData) {
        WsMessage message = null;
        try {
            message = createWsMessage(rawData);
        } catch (GlobalException e) {
            chatEndpoint.sendMessage(senderId, Response.fail(e.getErrno(), e.getErrmsg()));
            return;
        }
        if (!senderId.equals(message.getSenderId())) {
            String msg = "发送者与ws连接中的不一致,消息发送失败";
            log.info(msg);
            chatEndpoint.sendMessage(senderId, Response.fail(Response.SENDER_AND_WS_IS_NOT_MATCH, msg));
            return;
        }


        //更新数据库
        try {
            updateRedis(message);
        } catch (Exception e) {
            String msg = "添加聊天记录时发生异常,消息发送失败";
            log.info(msg);
            e.printStackTrace();
            chatEndpoint.sendMessage(senderId, Response.fail(Response.UPDATE_HISTORY_TO_SQL_FAIL, msg));
            return;
        }

        if (message.getMessageType() == MessageType.ESTABLISH_CHAT) {
            //首次发送,设为双方可见
            chatMapper.showToBoth(message.getChatId());
        }

        //如果接收方在线,转发ws消息到接收方
        if (chatEndpoint.hasConnect(message.getReceiverId())) {
            chatEndpoint.sendMessage(message.getReceiverId(), Response.ok(message));
        }

    }

    private void updateRedis(WsMessage message) {
        LastChat lastChat = redisClient.get(message.getChatId() + "");
        if (lastChat != null) {
            lastChat.setUnreadCount(lastChat.getUnreadCount() + 1);
            lastChat.setLastMsg(message);
        } else {
            lastChat = new LastChat();
            lastChat.setUnreadCount(1);
            lastChat.setLastMsg(message);
        }
        redisClient.set(message.getChatId() + "", lastChat);
    }


    private WsMessage createWsMessage(String rawData) throws GlobalException {
        WsMessage wsMessage = JsonUtil.fromJson(rawData, WsMessage.class);
        if (wsMessage == null) {
            String msg = "消息反序列化失败";
            log.info(msg);
            throw new GlobalException(Response.MESSAGE_FORMAT_IS_WRONG, msg);
        }
        if (ObjectUtils.isEmpty(wsMessage.getChatId()) ||
                StringUtils.isEmpty(wsMessage.getSenderId()) ||
                StringUtils.isEmpty(wsMessage.getReceiverId()) ||
                ObjectUtils.isEmpty(wsMessage.getGoodsId()) ||
                ObjectUtils.isEmpty(wsMessage.getMessageType()) ||
                ObjectUtils.isEmpty(wsMessage.getMessageBody())) {
            String msg = "消息不完整";
            log.info(msg);
            throw new GlobalException(Response.MESSAGE_IS_INCOMPLETE, msg);
        }

        if (wsMessage.getSendTime() == null) {
            wsMessage.setSendTime(new Date());
        }
        return wsMessage;
    }

}

package io.github.nnkwrik.imservice.service.impl;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.exception.GlobalException;
import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.imservice.constant.MessageType;
import io.github.nnkwrik.imservice.dao.ChatMapper;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.redis.RedisClient;
import io.github.nnkwrik.imservice.service.WebSocketService;
import io.github.nnkwrik.imservice.websocket.ChatEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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


    /**
     * 未读消息数
     *
     * @param userId
     * @return
     */
    @Override
    public int getUnreadCount(String userId) {
        //去查userId参与的chat的id
        List<Integer> chatIdList = chatMapper.getChatIdsByUser(userId);
        List<List<WsMessage>> unreadChats = redisClient.multiGet(chatIdList.stream()
                .map(id -> id + "")
                .collect(Collectors.toList()));

        //过滤自己发送的
        long unreadCount = unreadChats.stream()
                .filter(messageList -> !ObjectUtils.isEmpty(messageList))
                .flatMap(messageList -> messageList.stream())
                .filter(message -> message.getReceiverId().equals(userId))
                .count();

        return Math.toIntExact(unreadCount);
    }

    /**
     * 对客户端发送的websocket消息做处理
     *
     * @param senderId
     * @param rawData
     */
    @Override
    public void OnMessage(String senderId, String rawData) {
        WsMessage message = null;
        try {
            message = castWsMessage(rawData);
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


        //作为未读消息添加到redis
        updateRedis(message);

        if (message.getMessageType() == MessageType.FIRST_CHAT) {
            //首次发送,设为双方可见
            chatMapper.showToBoth(message.getChatId());
        }

        //如果接收方在线,转发ws消息到接收方
        if (chatEndpoint.hasConnect(message.getReceiverId())) {
            chatEndpoint.sendMessage(message.getReceiverId(), Response.ok(message));
        }

    }

    private void updateRedis(WsMessage message) {
        List<WsMessage> unreadList = redisClient.get(message.getChatId() + "");
        if (unreadList == null) {
            unreadList = new ArrayList<>();
        }
        unreadList.add(message);
        redisClient.set(message.getChatId() + "", unreadList);
    }


    private WsMessage castWsMessage(String rawData) throws GlobalException {
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

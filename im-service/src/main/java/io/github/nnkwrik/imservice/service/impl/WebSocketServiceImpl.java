package io.github.nnkwrik.imservice.service.impl;

import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.imservice.dao.HistoryMapper;
import io.github.nnkwrik.imservice.dao.UserMapper;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.service.WebSocketService;
import io.github.nnkwrik.imservice.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author nnkwrik
 * @date 18/12/05 12:30
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    private WebSocket webSocket;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public void sendMessage(String receiverId, String message) {
        WsMessage wsMessage = JsonUtil.fromJson(message,WsMessage.class);

        Integer unreadCount;
        if (wsMessage.getSenderId().compareTo(wsMessage.getReceiverId())>0){
            unreadCount = userMapper.getUnreadCountByChat(wsMessage.getReceiverId(),wsMessage.getSenderId());
        }else {
            unreadCount = userMapper.getUnreadCountByChat(wsMessage.getSenderId(),wsMessage.getReceiverId());
        }
        unreadCount++;

        if (webSocket.hasConnect(receiverId)){
            wsMessage.setUnreadCount(unreadCount);
            webSocket.sendMessage(receiverId,JsonUtil.toJson(wsMessage));
        }

        //更新数据库



    }

}

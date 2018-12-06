package io.github.nnkwrik.imservice.websocket;

import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.imservice.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author nnkwrik
 * @date 18/12/05 11:34
 */
@Component
@ServerEndpoint(value = "/ws/{openId}", configurator = ChatEndpointConfigure.class)
@Slf4j
public class ChatEndpoint {

    @Autowired
    private WebSocketService webSocketService;

    private static ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("openId") String openId, Session session) {
        sessionMap.put(openId, session);
        log.info("【websocket消息】有新的连接, openId = [{}]", openId);
    }

    @OnClose
    public void onClose(@PathParam("openId") String openId) {
        sessionMap.remove(openId);
        log.info("【websocket消息】连接断开, openId = [{}]", openId);
    }

    @OnMessage
    public void onMessage(@PathParam("openId") String sender, String message) {
        log.info("【websocket消息】收到客户端发来的消息:发送者 = [{}],消息内容 = [{}]", sender, message);
        if (webSocketService == null) {
            log.info("webSocketService为空");
        }
        webSocketService.OnMessage(sender, message);
    }

    public boolean sendMessage(String openId, Response response) {
        Session session = sessionMap.get(openId);

        if (session == null) {
            log.info("消息发送失败,不存在该session:openId = [{}],消息内容 = [{}]", openId, response);
            return false;
        } else {
            try {
                session.getBasicRemote().sendText(JsonUtil.toJson(response));
                log.info("消息发送成功:openId = [{}],消息内容 = [{}]", openId, response);
                return true;
            } catch (IOException e) {
                log.info("消息发送失败:openId = [{}],消息内容 = [{}]", openId, response);
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean hasConnect(String openId) {
        if (sessionMap.get(openId) != null) {
            return true;
        } else {
            return false;
        }
    }
}

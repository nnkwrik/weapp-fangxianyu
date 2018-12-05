package io.github.nnkwrik.imservice.websocket;

import lombok.extern.slf4j.Slf4j;
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
@ServerEndpoint("/im/{openId}")
@Slf4j
public class WebSocket {

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
    public void onMessage(@PathParam("openId") String openId, String message) {
        log.info("【websocket消息】收到客户端发来的消息:openId = [{}],消息内容 = [{}]", openId, message);
    }

    public boolean sendMessage(String openId, String message) {
        Session session = sessionMap.get(openId);
        if (session == null) {
            log.info("消息发送失败,不存在该session:openId = [{}],消息内容 = [{}]", openId, message);
            return false;
        } else {
            try {
                session.getBasicRemote().sendText(message);
                log.info("消息发送成功:openId = [{}],消息内容 = [{}]", openId, message);
                return true;
            } catch (IOException e) {
                log.info("消息发送失败:openId = [{}],消息内容 = [{}]", openId, message);
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

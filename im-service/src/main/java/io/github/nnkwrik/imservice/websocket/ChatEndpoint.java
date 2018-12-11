package io.github.nnkwrik.imservice.websocket;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.dto.Response;
import io.github.nnkwrik.common.token.TokenSolver;
import io.github.nnkwrik.common.util.JsonUtil;
import io.github.nnkwrik.imservice.constant.MessageType;
import io.github.nnkwrik.imservice.model.vo.WsMessage;
import io.github.nnkwrik.imservice.service.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
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

    @Autowired
    private TokenSolver tokenSolver;

    private static ConcurrentMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("openId") String openId, Session session, EndpointConfig config) throws IOException {
        String token = (String) config.getUserProperties().get(JWTUser.class.getName());
        JWTUser user = solveToken(token);

//        if (user == null || !user.getOpenId().equals(openId)) {
//            log.info("【websocket消息】token检验失败,拒绝连接, openId = [{}]", openId);
//            rejectConnect(session);
//            session.close();
//            return;
//        }
        sessionMap.put(openId, session);
//        log.info("【websocket消息】有新的连接, openId = [{}],用户昵称= [{}],未读消息数={}", openId, user.getNickName(),);
        int unreadCount = webSocketService.getUnreadCount(openId);
        log.info("【websocket消息】有新的连接, openId = [{}],用户昵称= [{}],未读消息数={}", openId, "", unreadCount);

        WsMessage wsMessage = new WsMessage();
        wsMessage.setMessageType(MessageType.UNREAD_NUM);
        wsMessage.setMessageBody(unreadCount + "");
        wsMessage.setSendTime(new Date());
        sendMessage(openId, Response.ok(wsMessage));
    }

    @OnClose
    public void onClose(@PathParam("openId") String openId) {
        sessionMap.remove(openId);
        log.info("【websocket消息】连接断开, openId = [{}]", openId);
    }

    @OnMessage
    public void onMessage(@PathParam("openId") String sender, String message) {

        log.info("【websocket消息】收到客户端发来的消息:发送者 = [{}],消息内容 = [{}]", sender, message);
        webSocketService.OnMessage(sender, message);
    }

    private JWTUser solveToken(String token) {
        JWTUser user = null;
        if (StringUtils.isEmpty(token)) {
            log.info("用户的Authorization头为空,无法获取jwt");
        } else {
            try {
                user = tokenSolver.solve(token);
            } catch (TokenExpiredException e) {
                log.info("jwt已过期，过期时间：{}", e.getMessage());
            } catch (Exception e) {
                log.info("jwt解析失败,jwt={}", token);
            }
        }
        return user;
    }

    private void rejectConnect(Session session) {
        Response response = Response.fail(Response.TOKEN_IS_WRONG, "token检验失败,拒绝ws连接");
        try {
            session.getBasicRemote().sendText(JsonUtil.toJson(response));
            log.info("[拒绝连接]通知发送成功");
        } catch (IOException e) {
            log.info("[拒绝连接]通知发送失败");
            e.printStackTrace();
        }

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

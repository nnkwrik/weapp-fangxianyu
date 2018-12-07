package io.github.nnkwrik.imservice.service;

import io.github.nnkwrik.imservice.model.vo.WsMessage;

/**
 * @author nnkwrik
 * @date 18/12/05 12:28
 */
public interface WebSocketService {
    void OnMessage(String senderId, String message);

}

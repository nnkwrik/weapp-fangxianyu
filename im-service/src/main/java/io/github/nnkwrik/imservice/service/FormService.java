package io.github.nnkwrik.imservice.service;

import io.github.nnkwrik.imservice.model.vo.ChatForm;
import io.github.nnkwrik.imservice.model.vo.WsMessage;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 22:36
 */
public interface FormService {

    ChatForm showForm(int chatId, String userId, int size, Date offsetTime);

//    void addMessageListToSQL(List<WsMessage> messageList);
}

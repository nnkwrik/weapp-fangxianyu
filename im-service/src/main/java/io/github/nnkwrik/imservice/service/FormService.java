package io.github.nnkwrik.imservice.service;

import io.github.nnkwrik.imservice.model.vo.ChatForm;

/**
 * @author nnkwrik
 * @date 18/12/07 22:36
 */
public interface FormService {
    
    ChatForm showForm(int chatId, String userId, int page, int size, int offset);
}

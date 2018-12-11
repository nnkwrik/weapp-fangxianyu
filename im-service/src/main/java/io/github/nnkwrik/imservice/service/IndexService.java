package io.github.nnkwrik.imservice.service;

import io.github.nnkwrik.imservice.model.vo.ChatIndex;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 16:31
 */
public interface IndexService {
    List<ChatIndex> showIndex(String userId, int size, Date offsetTime);

}

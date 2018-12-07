package io.github.nnkwrik.imservice.model.vo;

import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.model.po.History;
import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/12/07 15:56
 */
@Data
public class ChatIndex {
    private SimpleUser otherSide;
    private SimpleGoods goods;
    private Integer unreadCount;
    private History lastChat;
}

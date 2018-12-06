package io.github.nnkwrik.imservice.model.po;

import io.github.nnkwrik.imservice.model.vo.WsMessage;
import lombok.Data;

/**
 * 存入redis,展示最后一条信息的内容
 * @author nnkwrik
 * @date 18/12/06 10:02
 */
@Data
public class LastChat {
    private Integer unreadCount;
    private WsMessage lastMsg;
}

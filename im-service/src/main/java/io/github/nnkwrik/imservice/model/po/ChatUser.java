package io.github.nnkwrik.imservice.model.po;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/12/05 21:19
 */
@Data
public class ChatUser {
    private Integer id;
    private String u1;   //u1 < u2
    private String u2;
    private Boolean u1ToU2; //指最后一条信息
    private Integer unreadCount;
}

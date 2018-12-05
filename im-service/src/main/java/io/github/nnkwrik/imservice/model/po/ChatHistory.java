package io.github.nnkwrik.imservice.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/12/05 21:23
 */
@Data
public class ChatHistory {
    private Integer id;
    private Integer chatUserId;
    private Boolean u1ToU2;
    private Integer messageType;    //0:系统消息,1.用户消息
    private String hint;            //相关的商品id等
    private String messageBody;
    private Date createTime;
}

package io.github.nnkwrik.imservice.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/12/05 21:23
 */
@Data
public class History {
    private Integer id;
    private Integer chatId;
    private Boolean u1ToU2;
    private Integer messageType;    //   0:系统消息,1.用户消息,2.单方建立会话
    private String messageBody;
    private Date sendTime;
}

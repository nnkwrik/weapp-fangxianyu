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
    private Integer messageType;    //0:系统消息,1.用户消息,2.建立连接(u1点开了u2但没发消息, u1ToU2 = false,type = )
    private String messageBody;
    private Date sendTime;
}

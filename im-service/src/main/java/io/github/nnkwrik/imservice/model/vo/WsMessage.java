package io.github.nnkwrik.imservice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/12/05 21:37
 */
@Data
public class WsMessage {
    private Integer chatUserId;
    private String senderId;
    private String receiverId;
    private Integer unreadCount;
    private Integer totalUnreadCount;

    private Integer messageType;    //0:系统消息,1.用户消息
    private String hint;
    private String messageBody;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", locale = "CHINA", timezone = "Asia/Shanghai")
    private Date createTime;
}

package io.github.nnkwrik.imservice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * webSocket接受发送的消息,redis也用这个保存未读消息
 *
 * @author nnkwrik
 * @date 18/12/05 21:37
 */
@Data
public class WsMessage {
    private Integer chatId;
    private String senderId;
    private String receiverId;
    private Integer goodsId;

    /*参考 constant.MessageType类*/
    private Integer messageType;
    private String messageBody;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "CHINA", timezone = "Asia/Shanghai")
    private Date sendTime;
}

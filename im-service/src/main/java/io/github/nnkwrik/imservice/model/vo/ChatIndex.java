package io.github.nnkwrik.imservice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 消息列表页面
 *
 * @author nnkwrik
 * @date 18/12/17 10:34
 */
@Data
public class ChatIndex {

    /*各个消息*/
    private List<ChatIndexEle> chats;

    /*最后一条消息的发送时间*/
    @JsonFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date offsetTime;
}

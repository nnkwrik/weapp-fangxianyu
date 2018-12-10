package io.github.nnkwrik.imservice.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.github.nnkwrik.common.dto.SimpleGoods;
import io.github.nnkwrik.common.dto.SimpleUser;
import io.github.nnkwrik.imservice.model.po.History;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nnkwrik
 * @date 18/12/07 22:33
 */
@Data
public class ChatForm {
    private SimpleUser otherSide;
    private SimpleGoods goods;
    private Boolean isU1;
    private List<History> historyList;

    @JsonFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date offsetTime;
}

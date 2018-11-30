package io.github.nnkwrik.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author nnkwrik
 * @date 18/11/23 18:07
 */
@Data
public class SimpleUser {
    private String openId;
    private String nickName;
    private String avatarUrl;

    @JsonFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601)
    private Date registerTime;
}

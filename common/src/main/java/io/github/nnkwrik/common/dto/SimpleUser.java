package io.github.nnkwrik.common.dto;

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
    private Date registerTime;
}

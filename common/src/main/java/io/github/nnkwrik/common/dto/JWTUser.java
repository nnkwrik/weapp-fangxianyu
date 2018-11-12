package io.github.nnkwrik.common.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/12 14:12
 */
@Data
public class JWTUser {

    private String openId;
    private String nickName;
    private String avatarUrl;


}

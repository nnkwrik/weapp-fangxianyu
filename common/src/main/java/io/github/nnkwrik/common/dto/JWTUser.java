package io.github.nnkwrik.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nnkwrik
 * @date 18/11/12 14:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTUser {

    private String openId;
    private String nickName;
    private String avatarUrl;


}

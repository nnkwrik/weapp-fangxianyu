package io.github.nnkwrik.authservice.model.vo;

import io.github.nnkwrik.common.dto.JWTUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nnkwrik
 * @date 18/11/19 14:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthVo {
    private String token;
    private JWTUser userInfo;
}
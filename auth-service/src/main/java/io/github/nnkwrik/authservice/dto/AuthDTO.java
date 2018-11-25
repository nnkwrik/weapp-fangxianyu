package io.github.nnkwrik.authservice.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/12 15:49
 */
@Data
public class AuthDTO {
    private String code;
    private DetailAuthDTO detail;
    private String expiredToken;
}

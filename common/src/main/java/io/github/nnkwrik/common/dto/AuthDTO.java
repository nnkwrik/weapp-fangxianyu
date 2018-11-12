package io.github.nnkwrik.common.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/12 15:49
 */
@Data
public class AuthDTO {
    private String jsCode;
    private String signature;
    private String rawData;
//    private String encryptedData;
//    private String iv;
}

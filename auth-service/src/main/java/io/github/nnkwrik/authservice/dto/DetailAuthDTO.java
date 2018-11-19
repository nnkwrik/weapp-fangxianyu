package io.github.nnkwrik.authservice.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/19 14:13
 */
@Data
public class DetailAuthDTO {
    public String signature;
    public String rawData;
    public String encryptedData;
    public String iv;
}

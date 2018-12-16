package io.github.nnkwrik.authservice.service;

import io.github.nnkwrik.authservice.model.vo.AuthVo;

/**
 * @author nnkwrik
 * @date 18/11/24 14:46
 */
public interface AuthService {
    String setOpenId4Data(String rawData, String openId);

    AuthVo createToken(String userData);

}

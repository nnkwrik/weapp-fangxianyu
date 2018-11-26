package io.github.nnkwrik.common.exception;

import io.github.nnkwrik.common.dto.Response;
import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/24 12:44
 */
@Data
public class JWTException extends RuntimeException {
    public static final int TOKEN_IS_EMPTY = Response.TOKEN_IS_EMPTY;
    public static final int TOKEN_IS_EXPIRED = Response.TOKEN_IS_EXPIRED;
    public static final int TOKEN_IS_WRONG = Response.TOKEN_IS_WRONG;

    private int errno;
    private String errmsg;


    public JWTException(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }

}

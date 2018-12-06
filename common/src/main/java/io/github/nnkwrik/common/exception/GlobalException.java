package io.github.nnkwrik.common.exception;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/12/06 9:15
 */
@Data
public class GlobalException extends Exception {
    private int errno;
    private String errmsg;


    public GlobalException(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }
}

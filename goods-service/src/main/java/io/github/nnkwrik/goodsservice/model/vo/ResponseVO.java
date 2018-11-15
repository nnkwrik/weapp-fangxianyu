package io.github.nnkwrik.goodsservice.model.vo;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/14 21:07
 */
@Data
public class ResponseVO<T> {
    private int errno;
    private String errmsg;
    private T data;


    public ResponseVO(T data) {
        this.data = data;
    }

    public ResponseVO(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }


    public static <T> ResponseVO ok(T data) {
        return new ResponseVO(data);
    }

    public static ResponseVO fail(int errno, String errmsg) {
        return new ResponseVO(errno, errmsg);
    }

}

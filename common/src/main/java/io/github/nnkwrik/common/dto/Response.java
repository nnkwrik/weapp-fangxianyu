package io.github.nnkwrik.common.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/14 21:07
 */
@Data
public class Response<T> {

    public static final int WRONG_JS_CODE = 3001;
    public static final int CHECK_USER_WITH_SESSION_FAIL = 3002;

    private int errno;
    private String errmsg;
    private T data;


    public Response(T data) {
        this.data = data;
    }

    public Response(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }


    public static <T> Response ok(T data) {
        return new Response(data);
    }

    public static Response fail(int errno, String errmsg) {
        return new Response(errno, errmsg);
    }

}

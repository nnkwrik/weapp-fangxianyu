package io.github.nnkwrik.common.dto;

import lombok.Data;

/**
 * @author nnkwrik
 * @date 18/11/14 21:07
 */
@Data
public class Response<T> {

    //auth
    public static final int WRONG_JS_CODE = 3001;
    public static final int CHECK_USER_WITH_SESSION_FAIL = 3002;
    public static final int TOKEN_IS_EMPTY = 3003;
    public static final int TOKEN_IS_EXPIRED = 3004;
    public static final int TOKEN_IS_WRONG = 3005;

    //goods
    public static final int OPEN_ID_IS_EMPTY = 4001;
    public static final int COMMENT_INFO_INCOMPLETE = 4002;
    public static final int POST_INFO_INCOMPLETE = 4003;
    public static final int SELLER_AND_GOODS_IS_NOT_MATCH = 4004;

    //user
    public static final int USER_IS_NOT_EXIST = 3001;

    private int errno;
    private String errmsg;
    private T data;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(int errno, String errmsg) {
        this.errno = errno;
        this.errmsg = errmsg;
    }


    public static Response ok() {
        return new Response();
    }
    public static <T> Response ok(T data) {
        return new Response(data);
    }


    public static Response fail(int errno, String errmsg) {
        return new Response(errno, errmsg);
    }

}

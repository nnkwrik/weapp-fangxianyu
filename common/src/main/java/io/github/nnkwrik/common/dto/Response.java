package io.github.nnkwrik.common.dto;

/**
 * @author nnkwrik
 * @date 18/11/11 16:42
 */
public class Response<T> {
    /**
     * 服务器响应数据
     */
    private T payload;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    public Response() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public Response(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public Response(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public Response(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public Response(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public Response(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Response ok() {
        return new Response(true);
    }

    public static <T> Response ok(T payload) {
        return new Response(true, payload);
    }

    public static <T> Response ok(int code) {
        return new Response(true, null, code);
    }

    public static <T> Response ok(T payload, int code) {
        return new Response(true, payload, code);
    }

    public static Response fail() {
        return new Response(false);
    }

    public static Response fail(String msg) {
        return new Response(false, msg);
    }

    public static Response fail(int code) {
        return new Response(false, null, code);
    }

    public static Response fail(int code, String msg) {
        return new Response(false, msg, code);
    }

}

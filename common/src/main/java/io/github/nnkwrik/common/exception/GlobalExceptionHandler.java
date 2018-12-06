package io.github.nnkwrik.common.exception;

import io.github.nnkwrik.common.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author nnkwrik
 * @date 18/11/24 12:54
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(GlobalException.class)
    public Object handleJWTException(GlobalException e) {
        log.info("发生异常，errno = {},errmsg = {}", e.getErrno(), e.getErrmsg());
        return Response.fail(e.getErrno(), e.getErrmsg());
    }

    @ResponseBody
    @ExceptionHandler(JWTException.class)
    public Object handleJWTException(JWTException e) {
        log.info("发生JWTException，errno = {},errmsg = {}", e.getErrno(), e.getErrmsg());

        return Response.fail(e.getErrno(), e.getErrmsg());
    }
}

package io.github.nnkwrik.common.token.injection;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.github.nnkwrik.common.dto.JWTUser;
import io.github.nnkwrik.common.token.TokenSolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author nnkwrik
 * @date 18/11/24 9:43
 */
@Slf4j
@Component
public class JWTResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private TokenSolver tokenSolver;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(JWT.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("Authorization");
        if (token == null) {
            log.info("用户的Authorization头为空,无法获取jwt");
            return null;
        }
        JWTUser user = null;
        try {
            user = tokenSolver.solve(token);
        } catch (TokenExpiredException e) {
            log.info("jwt已过期，过期时间：{}", e.getMessage());
        } catch (Exception e) {
            log.info("jwt解析失败");
        }
        log.info("jwt解析结果为：{}", user);
        return user;
    }

}

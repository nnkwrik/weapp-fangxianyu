package io.github.nnkwrik.common.token.injection;

import java.lang.annotation.*;

/**
 * @author nnkwrik
 * @date 18/11/24 9:54
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JWT {

    boolean required() default true;
}

package com.security.demo.aop;

import java.lang.annotation.*;

/**
 * @author fanglingxiao
 * @date 2019/3/26
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Action {
    String required() default "";
}

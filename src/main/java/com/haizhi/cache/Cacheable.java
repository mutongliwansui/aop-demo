package com.haizhi.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author mtl
 * @Description:
 * @date 2020/4/28 16:32
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {

    String key() default ""; //支持${xx}

    int expire() default 60 * 1000; //60s

}

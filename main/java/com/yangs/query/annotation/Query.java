package com.yangs.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yangs
 * @Description:
 * @Date: 2021/9/2 10:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Query {
    RepositoryEnum queryType() default RepositoryEnum.EQ;
}

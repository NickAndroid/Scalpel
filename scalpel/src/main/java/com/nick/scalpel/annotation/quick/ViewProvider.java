package com.nick.scalpel.annotation.quick;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by guohao4 on 2016/6/17.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
public @interface ViewProvider {
    int id();
}

package com.nick.scalpel.annotation.quick;

import com.nick.scalpel.annotation.opt.Beta;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Beta
public @interface DataProvider {
    String name();
}
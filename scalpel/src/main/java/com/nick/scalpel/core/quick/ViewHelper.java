package com.nick.scalpel.core.quick;

import java.lang.reflect.Field;

/**
 * Created by guohao4 on 2016/6/20.
 */
public interface ViewHelper<T> {
    void doExtendedHelp(T view, Field field, Object targetObj);
}

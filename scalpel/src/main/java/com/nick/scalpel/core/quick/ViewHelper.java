package com.nick.scalpel.core.quick;

import java.lang.reflect.Field;

public interface ViewHelper<T> {
    void doExtendedHelp(T view, Field field, Object targetObj);
}

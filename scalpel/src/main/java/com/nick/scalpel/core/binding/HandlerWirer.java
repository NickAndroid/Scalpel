/*
 * Copyright (c) 2016 Nick Guo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nick.scalpel.core.binding;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nick.scalpel.config.Configuration;

import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

abstract class HandlerWirer extends AbsContextedFinder {

    public HandlerWirer(Configuration configuration) {
        super(configuration);
    }

    @NonNull
    protected abstract Looper getLooper(Object object, Field field);

    @Nullable
    protected abstract Handler.Callback getCallback(Object object, Field field);

    @Override
    public void wire(Context context, Object object, Field field) {
        makeAccessible(field);
        Object fieldObject = getField(field, object);
        if (fieldObject != null) return;

        Handler handler = new Handler(getLooper(object, field), getCallback(object, field));
        setField(field, object, handler);
    }
}

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

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nick.scalpel.annotation.binding.WorkerThreadHandler;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class WorkThreadHandlerWirer extends HandlerWirer {

    public WorkThreadHandlerWirer(Configuration configuration) {
        super(configuration);
    }

    @NonNull
    @Override
    protected Looper getLooper(Object object, Field field) {
        WorkerThreadHandler handler = field.getAnnotation(WorkerThreadHandler.class);
        String tName = handler.threadName();
        int priority = handler.priority();
        HandlerThread thread = new HandlerThread(tName, priority);
        thread.start();
        return thread.getLooper();
    }

    @Nullable
    @Override
    protected Handler.Callback getCallback(Object object, Field field) {
        WorkerThreadHandler handler = field.getAnnotation(WorkerThreadHandler.class);

        String callbackStr = handler.callback();
        if (TextUtils.isEmpty(callbackStr)) return null;

        Object fieldObj;

        switch (callbackStr) {
            case ThisThatNull.THIS:
                fieldObj = object;
                break;
            default:
                Field callbackField = ReflectionUtils.findField(object, callbackStr);
                Preconditions.checkNotNull(callbackField);
                ReflectionUtils.makeAccessible(callbackField);

                fieldObj = ReflectionUtils.getField(callbackField, object);
                Preconditions.checkNotNull(fieldObj);
                break;
        }

        boolean isCallback = fieldObj instanceof Handler.Callback;
        Preconditions.checkState(isCallback);

        return (Handler.Callback) fieldObj;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return WorkerThreadHandler.class;
    }
}

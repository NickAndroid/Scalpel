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

package com.nick.scalpel.intarnal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.intarnal.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class OnClickWirer implements FieldWirer {

    private AutoFoundWirer mAutoFoundWirer;
    private boolean debug;
    private String logTag;

    public OnClickWirer(AutoFoundWirer wirer, Configuration configuration) {
        this.mAutoFoundWirer = wirer;
        this.debug = configuration.isDebug();
        this.logTag = configuration.getLogTag();
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return OnClick.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, activity);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(activity, field);
        }
        autoWire(activity, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(context, object, field);
        }
        autoWire(object, field);
    }

    private void autoWire(final Object o, Field field) {

        if (debug) Log.d(logTag, "Auto wiring: " + field.getName());

        Object fieldObjectWired = ReflectionUtils.getField(field, o);
        if (fieldObjectWired == null) return;

        boolean isView = fieldObjectWired instanceof View;

        if (!isView)
            throw new IllegalArgumentException("Object " + fieldObjectWired + " is not instance of View.");

        View view = (View) fieldObjectWired;

        OnClick onClick = field.getAnnotation(OnClick.class);
        String listener = onClick.listener();
        String action = onClick.action();

        if (!TextUtils.isEmpty(listener)) {
            Field onClickListenerField = ReflectionUtils.findField(o, listener);
            if (onClickListenerField == null)
                throw new NullPointerException("No such listener:" + listener);

            ReflectionUtils.makeAccessible(onClickListenerField);

            Object onClickListenerObj = ReflectionUtils.getField(onClickListenerField, o);
            if (onClickListenerObj == null)
                throw new NullPointerException("Null listener:" + listener);

            boolean isListener = onClickListenerObj instanceof View.OnClickListener;

            if (!isListener)
                throw new IllegalArgumentException("Object " + onClickListenerObj + " is not instance of OnClickListener.");

            View.OnClickListener onClickListener = (View.OnClickListener) onClickListenerObj;

            view.setOnClickListener(onClickListener);

            if (debug) Log.d(logTag, "OnClickWirer listener, Auto wired: " + field.getName());
        } else if (!TextUtils.isEmpty(action)) {
            final String[] args = onClick.args();
            Class[] argClz = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClz[i] = String.class;
            }
            Method actionMethod = ReflectionUtils.findMethod(o.getClass(), action, argClz);
            if (actionMethod == null)
                throw new NullPointerException("No such method:" + action + " with args:" + Arrays.toString(argClz));
            ReflectionUtils.makeAccessible(actionMethod);
            final Method finalActionMethod = actionMethod;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReflectionUtils.invokeMethod(finalActionMethod, o, args);
                }
            });
            if (debug) Log.d(logTag, "OnClickWirer action, Auto wired: " + field.getName());
        }
    }

    @Override
    public void wire(View root, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(root, object, field);
        }

        autoWire(object, field);
    }

    private boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }
}

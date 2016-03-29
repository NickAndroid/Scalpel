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

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.nick.scalpel.annotation.binding.OnTouch;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsFieldWirer;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class OnTouchWirer extends AbsFieldWirer {

    private ViewFinder mAutoFinder;

    public OnTouchWirer(Configuration configuration) {
        super(configuration);
        this.mAutoFinder = new ViewFinder(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return OnTouch.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, activity);
        if (fieldObject == null) {
            mAutoFinder.wire(activity, field);
        }
        autoWire(activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, fragment);
        if (fieldObject == null) {
            mAutoFinder.wire(fragment, field);
        }
        autoWire(fragment, field);
    }

    @Override
    public void wire(Service service, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, service);
        if (fieldObject == null) {
            mAutoFinder.wire(service, field);
        }
        autoWire(service, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFinder.wire(context, object, field);
        }
        autoWire(object, field);
    }

    private void autoWire(final Object o, Field field) {

        logD("Auto wiring: " + field.getName());

        Object fieldObjectWired = ReflectionUtils.getField(field, o);
        if (fieldObjectWired == null) return;

        boolean isView = fieldObjectWired instanceof View;

        if (!isView)
            throw new IllegalArgumentException("Object " + fieldObjectWired + " is not instance of View.");

        View view = (View) fieldObjectWired;

        OnTouch onClick = field.getAnnotation(OnTouch.class);
        String listener = onClick.listener();
        String action = onClick.action();

        if (!TextUtils.isEmpty(listener)) {
            Field onTouchListenerField = ReflectionUtils.findField(o, listener);
            if (onTouchListenerField == null)
                throw new NullPointerException("No such listener:" + listener);

            ReflectionUtils.makeAccessible(onTouchListenerField);

            Object onTouchListenerObj = ReflectionUtils.getField(onTouchListenerField, o);
            if (onTouchListenerObj == null)
                throw new NullPointerException("Null listener:" + listener);

            boolean isListener = onTouchListenerObj instanceof View.OnTouchListener;

            if (!isListener)
                throw new IllegalArgumentException("Object " + onTouchListenerObj + " is not instance of OnTouchListener.");

            View.OnTouchListener onTouchListener = (View.OnTouchListener) onTouchListenerObj;

            view.setOnTouchListener(onTouchListener);

            logD("OnTouchWirer listener, Auto wired: " + field.getName());
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
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Object result = ReflectionUtils.invokeMethod(finalActionMethod, o, args);
                    logD("OnTouchWirer onTouch invoked: " + result);
                    return true;
                }
            });
            logD("OnTouchWirer actions, Auto wired: " + field.getName());
        }
    }

    @Override
    public void wire(View root, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFinder.wire(root, object, field);
        }

        autoWire(object, field);
    }
}

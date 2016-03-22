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

package com.nick.scalpel.core;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutoBindWirer extends AbsFieldWirer {

    public AutoBindWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoBind.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        wire(activity.getApplicationContext(), activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        wire(fragment.getActivity(), fragment, field);
    }

    @Override
    public void wire(Service service, Field field) {
        wire(service.getApplicationContext(), service, field);
    }

    @Override
    public void wire(Context context, final Object object, final Field field) {
        ReflectionUtils.makeAccessible(field);
        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject != null) return;

        // FIXME: 21/03/16 Ensure it is an AIDL service.
        boolean isIInterface = field.getType().isInterface();
        Preconditions.checkState(isIInterface, "Field:" + field.getName() + " is not an AIDL interface, is:" + field.getType());

        AutoBind autoBind = field.getAnnotation(AutoBind.class);
        String action = autoBind.action();
        String pkg = autoBind.pkg();
        int flags = autoBind.flags();
        String callback = autoBind.callback();
        boolean startService = autoBind.startService();
        boolean isExplicit = !TextUtils.isEmpty(action) && !TextUtils.isEmpty(pkg);
        Preconditions.checkState(isExplicit, "Action and PackageName should be specified");

        AutoBind.Callback callbackInstance = null;
        if (!TextUtils.isEmpty(callback)) {
            Object callbackObject = null;
            switch (callback) {
                case "this":
                    callbackObject = object;
                    break;
                default:
                    Field callbackField = ReflectionUtils.findField(object, callback);
                    if (callbackField != null) {
                        ReflectionUtils.makeAccessible(callbackField);
                        callbackObject = ReflectionUtils.getField(callbackField, object);
                    }
            }
            boolean isCallback = callbackObject instanceof AutoBind.Callback;
            Preconditions.checkState(isCallback, "Field:" + callback + " is not an instance of Callback.");
            callbackInstance = (AutoBind.Callback) callbackObject;
        }

        Intent intent = new Intent(action);
        intent.setPackage(pkg);

        if (startService) context.startService(intent);

        final AutoBind.Callback finalCallbackInstance = callbackInstance;
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Class serviceClass = field.getType();
                String stubClassName = serviceClass.getName() + "$Stub";
                try {
                    Class stubClass = Class.forName(stubClassName);
                    Method asInterface = ReflectionUtils.findMethod(stubClass, "asInterface", IBinder.class);
                    Object result = ReflectionUtils.invokeMethod(asInterface, null, service);
                    ReflectionUtils.setField(field, object, result);
                    // Callback result.
                    if (finalCallbackInstance != null)
                        finalCallbackInstance.onServiceBound(name, this);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (finalCallbackInstance != null)
                    finalCallbackInstance.onServiceDisconnected(name);
            }
        };
        //noinspection ResourceType
        context.bindService(intent, connection, flags);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        wire(root.getContext(), object, field);
    }
}

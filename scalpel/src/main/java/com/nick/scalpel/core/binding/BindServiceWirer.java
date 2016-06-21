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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;

import com.nick.scalpel.annotation.binding.BindService;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.LifeCycleCallbackAdapter;
import com.nick.scalpel.core.LifeCycleManager;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class BindServiceWirer extends AbsContextedFinder {

    LifeCycleManager mLifeCycleManager;

    public BindServiceWirer(Configuration configuration, LifeCycleManager manager) {
        super(configuration);
        this.mLifeCycleManager = manager;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return BindService.class;
    }

    @Override
    public void wire(final Context context, final Object object, final Field field) {
        ReflectionUtils.makeAccessible(field);
        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject != null) return;

        // FIXME: 21/03/16 Ensure it is an AIDL service.
        boolean isIInterface = field.getType().isInterface();
        Preconditions.checkState(isIInterface, "Field:" + field.getName() + " is not an AIDL interface, is:" + field.getType());

        BindService bindService = field.getAnnotation(BindService.class);
        String action = bindService.action();
        String pkg = bindService.pkg();
        int flags = bindService.flags();
        String callback = bindService.callback();
        boolean startService = bindService.startService();
        boolean isExplicit = !TextUtils.isEmpty(action) && !TextUtils.isEmpty(pkg);
        Preconditions.checkState(isExplicit, "Action and PackageName should be specified");

        boolean autoUnbind = bindService.autoUnbind();
        boolean isActivity = object instanceof Activity;
        Preconditions.checkState(!autoUnbind || isActivity, "Auto unbind only work for activities.");

        BindService.Callback callbackInstance = null;
        if (!TextUtils.isEmpty(callback)) {
            Object callbackObject = null;
            switch (callback) {
                case "this":
                    callbackObject = object;
                    break;
                default:
                    Field callbackField = ReflectionUtils.findField(object, callback);
                    Preconditions.checkNotNull(callbackField);
                    ReflectionUtils.makeAccessible(callbackField);
                    callbackObject = ReflectionUtils.getField(callbackField, object);
                    Preconditions.checkNotNull(callbackObject);
            }
            boolean isCallback = callbackObject instanceof BindService.Callback;
            Preconditions.checkState(isCallback, "Field:" + callback + " is not an instance of Callback.");
            callbackInstance = (BindService.Callback) callbackObject;
        }

        final Intent intent = new Intent(action);
        intent.setPackage(pkg);

        if (startService) context.startService(intent);

        final BindService.Callback finalCallbackInstance = callbackInstance;
        final ServiceConnection connection = new ServiceConnection() {
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
                        finalCallbackInstance.onServiceBound(name, this, intent);
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

        if (autoUnbind) {
            final String fieldName = field.getName();
            boolean registered = mLifeCycleManager.registerActivityLifecycleCallbacks(new LifeCycleCallbackAdapter() {
                @Override
                public void onActivityDestroyed(Activity activity) {
                    super.onActivityDestroyed(activity);
                    if (activity == object) {
                        logV("unBind service for: " + fieldName);
                        context.unbindService(connection);
                        mLifeCycleManager.unRegisterActivityLifecycleCallbacks(this);
                    }
                }
            });
            if (!registered) {
                logE("Failed to register life cycle callback!");
            }
        }
    }

    @Override
    public void wire(View root, Object object, Field field) {
        wire(root.getContext(), object, field);
    }
}

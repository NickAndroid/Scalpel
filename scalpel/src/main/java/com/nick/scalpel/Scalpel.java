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

package com.nick.scalpel;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.ClassWirer;
import com.nick.scalpel.core.FieldWirer;
import com.nick.scalpel.core.HandlerSupplier;
import com.nick.scalpel.core.LifeCycleManager;
import com.nick.scalpel.core.binding.Bindings;
import com.nick.scalpel.core.hook.Hooks;
import com.nick.scalpel.core.opt.Opts;
import com.nick.scalpel.core.opt.Recyclable;
import com.nick.scalpel.core.request.Requests;
import com.nick.scalpel.core.utils.Preconditions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Api class for Scalpel project.
 * To get the instance of Scalpel you can use {@link #getInstance()}
 * or create an instance manually.
 */
public class Scalpel implements LifeCycleManager, HandlerSupplier, Recyclable {

    private static Scalpel ourScalpel;

    private final Set<FieldWirer> mFieldWirer;
    private final Set<ClassWirer> mClassWirer;

    private Application mApp;
    private Handler mHandler;

    private Configuration mConfiguration;

    private String mLogTag;
    private boolean mDebug;

    private Scalpel(Application application) {
        mFieldWirer = new HashSet<>();
        mClassWirer = new HashSet<>();
        mHandler = new Handler();
        mApp = application;
    }

    /**
     * Get the default shared single instance of {@link Scalpel}
     *
     * @return The default instance of {@link Scalpel}
     */
    public static Scalpel getInstance() {
        return ourScalpel;
    }

    /**
     * Assign the application to {@link Scalpel}
     *
     * @param application Application instance of this app.
     * @return The instance of {@link Scalpel}
     * @see Application
     */
    public static Scalpel create(Application application) {
        Preconditions.checkNotNull(application);
        ourScalpel = new Scalpel(application);
        return ourScalpel;
    }

    public Application getApp() {
        return mApp;
    }

    public Configuration getConfiguration() {
        return mConfiguration;
    }

    public Scalpel config(Configuration configuration) {
        Configuration usingConfig = configuration == null ? Configuration.DEFAULT : configuration;
        mConfiguration = usingConfig;
        mLogTag = usingConfig.getLogTag();
        mDebug = usingConfig.isDebug();
        Bindings.publishTo(this);
        Opts.publishTo(this);
        Hooks.publishTo(this);
        Requests.publishTo(this);
        return this;
    }

    public void wire(Activity activity) {
        wire(activity, Scope.All);
    }

    public void wire(Activity activity, Scope scope) {
        if (isInScope(scope, Scope.Class)) wireClz(activity);
        if (isInScope(scope, Scope.Field)) {
            Class clz = activity.getClass();
            for (Field field : clz.getDeclaredFields()) {
                synchronized (mFieldWirer) {
                    for (FieldWirer wirer : mFieldWirer) {
                        if (mDebug)
                            Preconditions.checkNotNull(wirer.annotationClass(), "No annotation in:" + wirer);
                        if (field.isAnnotationPresent(wirer.annotationClass())) {
                            wirer.wire(activity, field);
                        }
                    }
                }
            }
        }
    }

    public void wire(Service service) {
        wire(service, Scope.All);
    }

    public void wire(Service service, Scope scope) {
        if (isInScope(scope, Scope.Class)) wireClz(service);
        if (isInScope(scope, Scope.Field)) {
            Class clz = service.getClass();
            for (Field field : clz.getDeclaredFields()) {
                synchronized (mFieldWirer) {
                    for (FieldWirer wirer : mFieldWirer) {
                        if (mDebug)
                            Preconditions.checkNotNull(wirer.annotationClass(), "No annotation in:" + wirer);
                        if (field.isAnnotationPresent(wirer.annotationClass())) {
                            wirer.wire(service, field);
                        }
                    }
                }
            }
        }
    }

    public void wire(Fragment fragment) {
        wire(fragment, Scope.All);
    }

    public void wire(Fragment fragment, Scope scope) {
        if (isInScope(scope, Scope.Class)) wireClz(fragment);
        if (isInScope(scope, Scope.Field)) {
            Class clz = fragment.getClass();
            for (Field field : clz.getDeclaredFields()) {
                synchronized (mFieldWirer) {
                    for (FieldWirer wirer : mFieldWirer) {
                        if (mDebug)
                            Preconditions.checkNotNull(wirer.annotationClass(), "No annotation in:" + wirer);
                        if (field.isAnnotationPresent(wirer.annotationClass())) {
                            wirer.wire(fragment, field);
                        }
                    }
                }
            }
        }
    }

    public void wire(Context context, Object target) {
        wire(context, target, Scope.All);
    }

    public void wire(Context context, Object target, Scope scope) {
        if (isInScope(scope, Scope.Class)) wireClz(target);
        if (isInScope(scope, Scope.Field)) {
            Class clz = target.getClass();
            for (Field field : clz.getDeclaredFields()) {
                synchronized (mFieldWirer) {
                    for (FieldWirer wirer : mFieldWirer) {
                        if (mDebug)
                            Preconditions.checkNotNull(wirer.annotationClass(), "No annotation in:" + wirer);
                        if (field.isAnnotationPresent(wirer.annotationClass())) {
                            wirer.wire(context, target, field);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void wire(View rootView, Object target) {
        wire(rootView, target, Scope.All);
    }

    public void wire(View rootView, Object target, Scope scope) {
        if (isInScope(scope, Scope.Class)) wireClz(target);
        if (isInScope(scope, Scope.Field)) {
            Class clz = target.getClass();
            for (Field field : clz.getDeclaredFields()) {
                synchronized (mFieldWirer) {
                    for (FieldWirer wirer : mFieldWirer) {
                        if (mDebug)
                            Preconditions.checkNotNull(wirer.annotationClass(), "No annotation in:" + wirer);
                        if (field.isAnnotationPresent(wirer.annotationClass())) {
                            wirer.wire(rootView, target, field);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isInScope(Scope given, Scope expected) {
        Preconditions.checkNotNull(given);
        switch (expected) {
            case Class:
                return given == Scope.Class || given == Scope.All;
            case Field:
                return given == Scope.Field || given == Scope.All;
            case All:
                return given == Scope.All;
            default:
                return false;
        }
    }

    private void wireClz(Object o) {
        Class clz = o.getClass();
        synchronized (mClassWirer) {
            for (ClassWirer clzWirer : mClassWirer) {
                if (mDebug)
                    Preconditions.checkNotNull(clzWirer.annotationClass(), "No annotation in:" + clzWirer);
                if (clz.isAnnotationPresent(clzWirer.annotationClass())) {
                    clzWirer.wire(o);
                }
            }
        }
    }

    public LifeCycleManager getLifeCycleManager() {
        return this;
    }

    @Override
    public boolean registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        if (mApp != null) {
            mApp.registerActivityLifecycleCallbacks(callback);
            return true;
        }
        Log.e(mLogTag, "Application not specify, call Scalpel.application() to set one!");
        return false;
    }

    @Override
    public boolean unRegisterActivityLifecycleCallbacks(final Application.ActivityLifecycleCallbacks callback) {
        // Post this task instead of directly call to avoid List modify errors.
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mApp.unregisterActivityLifecycleCallbacks(callback);
            }
        });
        return true;
    }

    @NonNull
    @Override
    public Handler getHandler() {
        return mHandler;
    }

    public Scalpel addFieldWirer(@NonNull FieldWirer wirer) {
        Preconditions.checkNotNull(wirer);
        synchronized (mFieldWirer) {
            mFieldWirer.add(wirer);
        }
        return this;
    }

    public Scalpel addClassWirer(@NonNull ClassWirer wirer) {
        Preconditions.checkNotNull(wirer);
        synchronized (mClassWirer) {
            mClassWirer.add(wirer);
        }
        return this;
    }

    @Override
    public void recycle() {
        mClassWirer.clear();
        mFieldWirer.clear();
        mConfiguration = null;
        mHandler = null;
        mApp = null;
        mLogTag = null;
    }
}

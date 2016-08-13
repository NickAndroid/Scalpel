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

package com.nick.scalpel.core.opt;

import android.app.Activity;

import com.nick.scalpel.annotation.opt.AutoRecycle;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.LifeCycleCallbackAdapter;
import com.nick.scalpel.core.LifeCycleManager;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class AutoRecycleWirer extends AbsOptWirer {

    LifeCycleManager mLifeCycleManager;

    public AutoRecycleWirer(Configuration configuration, LifeCycleManager manager) {
        super(configuration);
        this.mLifeCycleManager = manager;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoRecycle.class;
    }

    @Override
    public void wire(final Activity activity, final Field field) {
        ReflectionUtils.makeAccessible(field);
        final String fieldName = field.getName();
        boolean registered = mLifeCycleManager.registerActivityLifecycleCallbacks(new LifeCycleCallbackAdapter() {
            @Override
            public void onActivityDestroyed(Activity destroyed) {
                super.onActivityDestroyed(destroyed);
                if (destroyed == activity) {
                    logV("Recycling field: " + fieldName);
                    Object fieldObj = ReflectionUtils.getField(field, activity);
                    if (fieldObj == null) return;
                    try {
                        if (fieldObj instanceof AutoRecycle.Recyclable) {
                            ((AutoRecycle.Recyclable) fieldObj).recycle();
                        }
                    } catch (Exception ignored) {

                    } finally {
                        ReflectionUtils.setField(field, activity, null);
                        mLifeCycleManager.unRegisterActivityLifecycleCallbacks(this);
                    }
                }
            }
        });
        if (!registered) {
            logE("Failed to register life cycle callback!");
        }
    }
}

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
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsFieldWirer;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AutoRecycleWirer extends AbsFieldWirer {

    LifeCycleManager mLifeCycleManager;

    public AutoRecycleWirer(Configuration configuration, LifeCycleManager manager) {
        super(configuration);
        this.mLifeCycleManager = manager;
    }


    public AutoRecycleWirer(Configuration configuration) {
        super(configuration);
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
                    logV("Recycle field: " + fieldName);
                    ReflectionUtils.setField(field, activity, null);
                    mLifeCycleManager.unRegisterActivityLifecycleCallbacks(this);
                }
            }
        });
        if (!registered) {
            logE("Failed to register life cycle callback!");
        }
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        throw new UnsupportedOperationException("AutoRecycle is only supported in Activity.");
    }

    @Override
    public void wire(Service service, Field field) {
        throw new UnsupportedOperationException("AutoRecycle is only supported in Activity.");
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        throw new UnsupportedOperationException("AutoRecycle is only supported in Activity.");
    }

    @Override
    public void wire(View root, Object object, Field field) {
        throw new UnsupportedOperationException("AutoRecycle is only supported in Activity.");
    }
}

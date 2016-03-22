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
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AutoBindWirer;
import com.nick.scalpel.core.AutoFoundWirer;
import com.nick.scalpel.core.AutoRegisterWirer;
import com.nick.scalpel.core.AutoRequestPermissionWirer;
import com.nick.scalpel.core.ClassWirer;
import com.nick.scalpel.core.FieldWirer;
import com.nick.scalpel.core.OnClickWirer;
import com.nick.scalpel.core.OnTouchWirer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Api class for Scalpel project.
 * To get the instance of Scalpel you can use {@link #getDefault()}
 * or create an instance manually.
 */
public class Scalpel {

    private static Scalpel ourInjection = new Scalpel();

    private final Set<FieldWirer> mFieldWirers;
    private final Set<ClassWirer> mClassWirers;

    public Scalpel() {
        mFieldWirers = new HashSet<>();
        mClassWirers = new HashSet<>();
    }

    public static Scalpel getDefault() {
        return ourInjection;
    }

    public void config(Configuration configuration) {
        Configuration usingConfig = configuration == null ? Configuration.DEFAULT : configuration;
        AutoFoundWirer autoFoundWirer = new AutoFoundWirer(usingConfig);
        mFieldWirers.add(autoFoundWirer);
        mFieldWirers.add(new OnClickWirer(autoFoundWirer, usingConfig));
        mFieldWirers.add(new OnTouchWirer(autoFoundWirer, usingConfig));
        mFieldWirers.add(new AutoBindWirer(usingConfig));
        mFieldWirers.add(new AutoRegisterWirer(usingConfig));

        mClassWirers.add(new AutoRequestPermissionWirer(usingConfig));
    }

    public void wire(Activity activity) {
        wireClz(activity);
        Class clz = activity.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mFieldWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(activity, field);
                }
            }
        }
    }

    public void wire(Service service) {
        wireClz(service);
        Class clz = service.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mFieldWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(service, field);
                }
            }
        }
    }

    public void wire(Fragment fragment) {
        wireClz(fragment);
        Class clz = fragment.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mFieldWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(fragment, field);
                }
            }
        }
    }

    public void wire(Context context, Object target) {
        wireClz(target);
        Class clz = target.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mFieldWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(context, target, field);
                    break;
                }
            }
        }
    }

    public void wire(View rootView, Object target) {
        wireClz(target);
        Class clz = target.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mFieldWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(rootView, target, field);
                    break;
                }
            }
        }
    }

    private void wireClz(Object o) {
        Class clz = o.getClass();
        for (ClassWirer clzWirer : mClassWirers) {
            if (clz.isAnnotationPresent(clzWirer.annotationClass())) {
                clzWirer.wire(o);
            }
        }
    }
}

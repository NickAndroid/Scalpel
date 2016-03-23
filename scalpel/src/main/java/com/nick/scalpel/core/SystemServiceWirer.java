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
import android.content.Context;
import android.os.IBinder;
import android.os.IPowerManager;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.os.ServiceManager;
import com.nick.scalpel.core.os.ServiceType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.isBaseDataType;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

public class SystemServiceWirer extends AbsFieldWirer {

    ServiceManager mServiceManager;

    public SystemServiceWirer(Configuration configuration, ServiceManager serviceManager) {
        super(configuration);
        this.mServiceManager = serviceManager;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return SystemService.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        blindWire(activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        blindWire(fragment, field);
    }

    @Override
    public void wire(Service service, Field field) {
        blindWire(service, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        blindWire(context, field);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        blindWire(object, field);
    }

    private void blindWire(Object object, Field field) {
        ServiceType serviceType = getServiceType(object, field);
        if (serviceType == null) return;
        IBinder binder = (IBinder) mServiceManager.getServiceBinder(serviceType);
        logV("Found binder:" + binder + ", for:" + field);
        if (binder == null) {
            logE("Failed to wire system service:" + field);
            return;
        }
        switch (serviceType) {
            case POWER:
                setField(field, object, IPowerManager.Stub.asInterface(binder));
                break;
            case PACKAGE:
                break;
        }
    }

    protected ServiceType getServiceType(Object object, Field field) {
        makeAccessible(field);

        Object fieldObject = getField(field, object);

        boolean isBaseType = false;

        if (fieldObject != null)
            isBaseType = isBaseDataType(fieldObject.getClass());
        if (fieldObject != null && !isBaseType) return null;

        SystemService service = field.getAnnotation(SystemService.class);
        ServiceType who = service.service();

        if (who == ServiceType.AUTO) {
            who = autoDetermineServiceType(field.getType());
            if (who == null) return null;
            return who;
        } else if (isTypeOf(field.getType(), who.clz))
            return who;
        return null;
    }

    protected ServiceType autoDetermineServiceType(Class clz) {
        ServiceType[] all = ServiceType.values();
        int matchCnt = 0;
        ServiceType serviceType = null;
        for (ServiceType s : all) {
            if (isTypeOf(clz, s.clz)) {
                matchCnt++;
                if (matchCnt > 1) return null;
                serviceType = s;
            }
        }
        return serviceType;
    }


    protected boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }
}

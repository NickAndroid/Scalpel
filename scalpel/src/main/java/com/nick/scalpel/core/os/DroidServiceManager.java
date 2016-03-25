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

package com.nick.scalpel.core.os;

import android.os.IBinder;

import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.reflect.Method;

public class DroidServiceManager implements ServiceManager {

    Method getServiceMethod = null;

    public DroidServiceManager() throws Exception {
        Class serviceManagerClass = Class.forName("android.os.ServiceManager");
        getServiceMethod = ReflectionUtils.findMethod(serviceManagerClass, "getService", String.class);
    }

    @Override
    public IBinder getServiceBinder(ServiceType name) {
        Object result = ReflectionUtils.invokeMethod(getServiceMethod, null, name.name);
        return result == null ? null : (IBinder) result;
    }
}

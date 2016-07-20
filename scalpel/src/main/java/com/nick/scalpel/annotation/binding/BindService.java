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

package com.nick.scalpel.annotation.binding;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.nick.scalpel.annotation.opt.Since;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Since("1.0.7")
public @interface BindService {

    String action();

    String pkg();

    int flags() default Context.BIND_AUTO_CREATE;

    String callback() default "";

    boolean startService() default false;

    boolean autoUnbind() default false;

    interface Callback {
        void onServiceBound(ComponentName name, ServiceConnection connection, Intent intent);

        void onServiceDisconnected(ComponentName name);
    }
}

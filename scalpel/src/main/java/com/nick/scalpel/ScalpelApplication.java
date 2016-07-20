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

import android.app.Application;
import android.support.annotation.CallSuper;
import android.util.Log;

import com.nick.scalpel.annotation.opt.ContextConfiguration;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.ClassWirer;

import java.lang.annotation.Annotation;

public class ScalpelApplication extends Application {

    private int mContextConfigurationRes = -1;

    @Override
    @CallSuper
    public void onCreate() {
        super.onCreate();
        new ContextConfigurationProcessor().wire(this);
        Scalpel scalpel = Scalpel.create(this)
                .config(Configuration.builder()
                        .debug(true)
                        .beanContextRes(mContextConfigurationRes)
                        .logTag(getClass().getSimpleName())
                        .build());
        onConfigScalpel(scalpel);
    }

    protected void onConfigScalpel(Scalpel scalpel) {
        // Noop.
    }

    private class ContextConfigurationProcessor implements ClassWirer {

        @Override
        public void wire(Object o) {
            if (o instanceof ScalpelApplication) {
                ContextConfiguration contextConfiguration = o.getClass().getAnnotation(ContextConfiguration.class);
                if (contextConfiguration == null) return;
                ((ScalpelApplication) o).mContextConfigurationRes = (contextConfiguration.xmlRes());
                Log.d(getClass().getSimpleName(), "Bring up app with context config res:" + contextConfiguration.xmlRes());
            }
        }

        @Override
        public Class<? extends Annotation> annotationClass() {
            return ContextConfiguration.class;
        }
    }
}

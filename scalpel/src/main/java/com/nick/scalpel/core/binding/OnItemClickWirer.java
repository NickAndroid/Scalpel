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
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.annotation.binding.OnItemClick;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsFieldWirer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class OnItemClickWirer extends AbsFieldWirer {

    public OnItemClickWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return OnItemClick.class;
    }

    @Override
    public void wire(Activity activity, Field field) {

    }

    @Override
    public void wire(Fragment fragment, Field field) {

    }

    @Override
    public void wire(Service service, Field field) {

    }

    @Override
    public void wire(Context context, Object object, Field field) {

    }

    @Override
    public void wire(View root, Object object, Field field) {

    }
}

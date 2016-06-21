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
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

class ViewFinder extends AbsFinder {

    public ViewFinder(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return FindView.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        wire(activity.getWindow().getDecorView(), activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        wire(fragment.getView(), fragment, field);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        makeAccessible(field);
        Object fieldObject = getField(field, object);
        if (fieldObject != null) return;
        FindView findView = field.getAnnotation(FindView.class);
        int id = findView.id();
        if (id <= 0) invalidArg();
        View foundView = root.findViewById(id);
        setField(field, object, foundView);
        onViewFound(foundView, field, object);
    }

    protected void onViewFound(View view, Field field, Object targetObj) {
        // Nothing.
    }
}

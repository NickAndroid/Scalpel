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

import android.content.Context;

import com.nick.scalpel.annotation.binding.FindIntArray;
import com.nick.scalpel.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

class IntArrayFinder extends AbsIdFinder {

    public IntArrayFinder(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected int getIdRes(Field field) {
        FindIntArray findIntArray = field.getAnnotation(FindIntArray.class);
        return findIntArray.id();
    }

    @Override
    protected void wireFromId(int id, Context context, Object object, Field field) {
        setField(field, object, context.getResources().getIntArray(id));
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return FindIntArray.class;
    }
}

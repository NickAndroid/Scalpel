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

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

@InterfaceIgnore
public class RecyclerManager implements Recycler {

    @Bean
    private BitmapRecycler mBmRecycler;
    @Bean
    private GeneralRecycler mGeneralRecycler;
    @Bean
    private CollectionRecycler mCollectionRecycler;

    public RecyclerManager(Context context) {
        Scalpel.getDefault().wire(context, this);
        Log.d(getClass().getSimpleName(), "Create RecyclerManager:" + toString());
    }

    @Override
    public void recycle(@NonNull Object o) {
        Log.d(getClass().getSimpleName(), "Recycling:" + o);
        Class clz = o.getClass();
        for (Field field : clz.getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            Object fieldObj = ReflectionUtils.getField(field, o);
            if (field == null) return;
            if (ReflectionUtils.isBaseDataType(field.getType())) return;
            //noinspection unchecked
            findRecycler(fieldObj).recycle(fieldObj);
        }
    }

    private Recycler findRecycler(Object o) {
        if (o instanceof Collection) return mCollectionRecycler;
        if (o instanceof Bitmap) return mBmRecycler;
        return mGeneralRecycler;
    }
}

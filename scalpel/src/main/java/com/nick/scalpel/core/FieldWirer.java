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
import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldWirer {

    Class<? extends Annotation> annotationClass();

    void wire(Activity activity, Field field);

    void wire(Fragment fragment, Field field);

    void wire(Service service, Field field);

    void wire(Context context, Object object, Field field);

    void wire(View root, Object object, Field field);
}

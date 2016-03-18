/**
 * Copyright(c) 2016 Yangzhou New Telecom Science & Technology Co., Ltd. All rights served.
 */
package com.nick.scalpel.intarnal;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldWirer {
    Class<? extends Annotation> annotationClass();

    void wire(Activity activity, Field field);

    void wire(Context context, Object object, Field field);

    void wire(View root, Object object, Field field);
}

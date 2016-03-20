package com.nick.scalpel.intarnal;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nick.scalpel.intarnal.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class OnClickWirer implements FieldWirer {

    private AutoFoundWirer mAutoFoundWirer;

    public OnClickWirer(AutoFoundWirer wirer) {
        this.mAutoFoundWirer = wirer;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return OnClick.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, activity);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(activity, field);
        }
        autoWire(activity, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(context, object, field);
        }
        autoWire(object, field);
    }

    private void autoWire(Object o, Field field) {
        Object fieldObjectWired = ReflectionUtils.getField(field, o);
        if (fieldObjectWired == null) return;

        boolean isView = fieldObjectWired instanceof View;

        if (!isView)
            throw new IllegalArgumentException("Object " + fieldObjectWired + " is not instance of View.");

        View view = (View) fieldObjectWired;

        OnClick onClick = field.getAnnotation(OnClick.class);
        String listener = onClick.listener();
        if (TextUtils.isEmpty(listener)) return;

        Field onClickListenerField = ReflectionUtils.findField(o, listener);
        if (onClickListenerField == null)
            throw new NullPointerException("No such listener:" + listener);

        ReflectionUtils.makeAccessible(onClickListenerField);

        Object onClickListenerObj = ReflectionUtils.getField(onClickListenerField, o);
        if (onClickListenerObj == null) throw new NullPointerException("Null listener:" + listener);

        boolean isListener = onClickListenerObj instanceof View.OnClickListener;

        if (!isListener)
            throw new IllegalArgumentException("Object " + onClickListenerObj + " is not instance of OnClickListener.");

        View.OnClickListener onClickListener = (View.OnClickListener) onClickListenerObj;

        view.setOnClickListener(onClickListener);

        Log.d("Scalpel", "OnClickWirer, Auto wired: " + field.getName());
    }

    @Override
    public void wire(View root, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        if (fieldObject == null) {
            mAutoFoundWirer.wire(root, object, field);
        }

        autoWire(object, field);
    }

    private boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }
}

package com.nick.scalpel.intarnal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;

import com.nick.scalpel.intarnal.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AutoFoundWirer implements FieldWirer {

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoFound.class;
    }

    @Override
    public void wire(Activity activity, Field field) {

        WireParam param = getParam(activity, field);
        if (param == null) return;
        switch (param.type) {
            case View:
                wire(activity.getWindow().getDecorView(), activity, field);
                break;
            case String:
            case Color:
            case Integer:
            case Bool:
            case StringArray:
            case IntArray:
                wire(activity.getApplicationContext(), activity, field);
                break;
        }
    }

    private void wireFromRes(Resources resources, Type type, int idRes, Resources.Theme theme, Field field, Object forWho) {
        switch (type) {
            case String:
                ReflectionUtils.setField(field, forWho, resources.getString(idRes));
                break;
            case Color:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ReflectionUtils.setField(field, forWho, resources.getColor(idRes, theme));
                } else {
                    ReflectionUtils.setField(field, forWho, resources.getColor(idRes));
                }
                break;
            case Integer:
                ReflectionUtils.setField(field, forWho, resources.getInteger(idRes));
                break;
            case Bool:
                ReflectionUtils.setField(field, forWho, resources.getBoolean(idRes));
                break;
            case StringArray:
                ReflectionUtils.setField(field, forWho, resources.getStringArray(idRes));
                break;
            case IntArray:
                ReflectionUtils.setField(field, forWho, resources.getIntArray(idRes));
                break;
        }
    }

    private WireParam getParam(Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);

        boolean isBaseType = false;

        if (fieldObject != null)
            isBaseType = ReflectionUtils.isBaseDataType(fieldObject.getClass());
        if (fieldObject != null && !isBaseType) return null;

        AutoFound found = field.getAnnotation(AutoFound.class);
        Type type = found.type();

        if (type == Type.Auto) {
            type = autoDetermineType(field.getType());
            if (type == null) return null;
        } else if (isTypeOf(field.getType(), type.targetClass))
            return new WireParam(type, found.id());
        return null;
    }

    private Type autoDetermineType(Class clz) {
        Type[] all = Type.values();
        int matchCnt = 0;
        Type found = null;
        for (Type t : all) {
            if (isTypeOf(clz, t.targetClass)) {
                matchCnt++;
                if (matchCnt > 1) return null;
                found = t;
            }
        }
        return found;
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        WireParam param = getParam(object, field);
        if (param == null) return;
        wireFromRes(context.getResources(), param.type, param.idRes, null, field, object);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        WireParam param = getParam(object, field);
        if (param == null) return;
        switch (param.type) {
            case View:
                ReflectionUtils.setField(field, object, root.findViewById(param.idRes));
                break;
        }
    }

    private boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }

    class WireParam {
        Type type;
        int idRes;

        public WireParam(Type type, int idRes) {
            this.type = type;
            this.idRes = idRes;
        }
    }
}

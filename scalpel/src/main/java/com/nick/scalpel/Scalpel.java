package com.nick.scalpel;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.nick.scalpel.intarnal.AutoFoundWirer;
import com.nick.scalpel.intarnal.FieldWirer;
import com.nick.scalpel.intarnal.OnClickWirer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Scalpel {

    private static Scalpel ourInjection = new Scalpel();

    private final Set<FieldWirer> mWirers;

    public Scalpel() {
        mWirers = new HashSet<>();
        AutoFoundWirer autoFoundWirer = new AutoFoundWirer();
        mWirers.add(autoFoundWirer);
        mWirers.add(new OnClickWirer(autoFoundWirer));
    }

    public static Scalpel getDefault() {
        return ourInjection;
    }

    public void wire(Activity activity) {
        Class clz = activity.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(activity, field);
                    break;
                }
            }
        }
    }

    public void wire(Context context, Object target) {
        Class clz = target.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(context, target, field);
                    break;
                }
            }
        }
    }

    public void wire(View rootView, Object target) {
        Class clz = target.getClass();
        for (Field field : clz.getDeclaredFields()) {
            for (FieldWirer wirer : mWirers) {
                if (field.isAnnotationPresent(wirer.annotationClass())) {
                    wirer.wire(rootView, target, field);
                    break;
                }
            }
        }
    }
}

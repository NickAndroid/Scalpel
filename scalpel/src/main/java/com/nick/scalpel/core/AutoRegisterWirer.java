package com.nick.scalpel.core;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@NotTested
public class AutoRegisterWirer extends AbsFieldWirer {

    public AutoRegisterWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoRegister.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        wire(activity.getApplicationContext(), activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        wire(fragment.getActivity(), fragment, field);
    }

    @Override
    public void wire(Service service, Field field) {
        wire(service.getApplicationContext(), service, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        Preconditions.checkNotNull(fieldObject, "Null field:" + field);

        boolean isReceive = fieldObject instanceof BroadcastReceiver;
        Preconditions.checkState(isReceive, "Not a receiver:" + field);

        AutoRegister autoRegister = field.getAnnotation(AutoRegister.class);
        String[] actions = autoRegister.actions();

        boolean hasAction = actions.length > 0 && !TextUtils.isEmpty(actions[0]);
        Preconditions.checkState(hasAction, "Invalid actions:" + Arrays.toString(actions));

        BroadcastReceiver receiver = (BroadcastReceiver) fieldObject;

        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }

        context.registerReceiver(receiver, filter);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        wire(root.getContext(), object, field);
    }
}

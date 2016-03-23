package com.nick.scalpel.core;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@Beta
public class AutoRegisterWirer extends AbsFieldWirer {

    LifeCycleManager mLifeCycleManager;

    public AutoRegisterWirer(Configuration configuration, LifeCycleManager manager) {
        super(configuration);
        this.mLifeCycleManager = manager;
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
    public void wire(final Context context, final Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        Preconditions.checkNotNull(fieldObject, "Null field:" + field);

        boolean isReceive = fieldObject instanceof BroadcastReceiver;
        Preconditions.checkState(isReceive, "Not a receiver:" + field);

        AutoRegister autoRegister = field.getAnnotation(AutoRegister.class);
        String[] actions = autoRegister.actions();
        boolean autoUnRegister = autoRegister.autoUnRegister();

        boolean isActivity = object instanceof Activity;
        Preconditions.checkState(!autoUnRegister || isActivity, "Auto unregister only work for activities.");

        boolean hasAction = actions.length > 0 && !TextUtils.isEmpty(actions[0]);
        Preconditions.checkState(hasAction, "Invalid actions:" + Arrays.toString(actions));

        final BroadcastReceiver receiver = (BroadcastReceiver) fieldObject;

        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }

        context.registerReceiver(receiver, filter);

        if (autoUnRegister) {
            final String fieldName = field.getName();
            boolean registered = mLifeCycleManager.registerActivityLifecycleCallbacks(new LifeCycleCallbackAdapter() {
                @Override
                public void onActivityDestroyed(Activity activity) {
                    super.onActivityDestroyed(activity);
                    if (activity == object) {
                        Log.v(logTag, "UnRegister receiver for: " + fieldName);
                        context.unregisterReceiver(receiver);
                        mLifeCycleManager.unRegisterActivityLifecycleCallbacks(this);
                    }
                }
            });
            if (!registered) {
                Log.e(logTag, "Failed to register life cycle callback!");
            }
        }
    }

    @Override
    public void wire(View root, Object object, Field field) {
        wire(root.getContext(), object, field);
    }
}

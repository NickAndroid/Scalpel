package com.nick.scalpel.core.binding;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.nick.scalpel.annotation.binding.RegisterReceiver;
import com.nick.scalpel.annotation.opt.Beta;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.LifeCycleCallbackAdapter;
import com.nick.scalpel.core.LifeCycleManager;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@Beta
public class RegisterReceiverWirer extends AbsContextedFinder {

    LifeCycleManager mLifeCycleManager;

    public RegisterReceiverWirer(Configuration configuration, LifeCycleManager manager) {
        super(configuration);
        this.mLifeCycleManager = manager;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RegisterReceiver.class;
    }

    @Override
    public void wire(final Context context, final Object object, Field field) {
        ReflectionUtils.makeAccessible(field);

        Object fieldObject = ReflectionUtils.getField(field, object);
        Preconditions.checkNotNull(fieldObject, "Null field:" + field);

        boolean isReceive = fieldObject instanceof BroadcastReceiver;
        Preconditions.checkState(isReceive, "Not a receiver:" + field);

        RegisterReceiver registerReceiver = field.getAnnotation(RegisterReceiver.class);
        String[] actions = registerReceiver.actions();
        boolean autoUnRegister = registerReceiver.autoUnRegister();

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
                        logV("UnRegister receiver for: " + fieldName);
                        context.unregisterReceiver(receiver);
                        mLifeCycleManager.unRegisterActivityLifecycleCallbacks(this);
                    }
                }
            });
            if (!registered) {
                logE("Failed to register life cycle callback!");
            }
        }
    }
}

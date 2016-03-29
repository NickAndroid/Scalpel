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
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;

import com.nick.scalpel.annotation.binding.AutoWired;
import com.nick.scalpel.config.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

class ComponentWirer extends AbsContextedFinder {

    public ComponentWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoWired.class;
    }

    @Override
    public void wire(Context context, Object forWho, Field field) {
        makeAccessible(field);
        Object fieldObject = getField(field, forWho);
        if (fieldObject != null) return;

        Findable.Type type = determineType(field.getType());

        if (type == null) unSupport("Field type of " + field.getType() + " is not supported.");

        assert type != null;
        switch (type) {
            case PM:
                setField(field, forWho, context.getSystemService(Context.POWER_SERVICE));
                break;
            case ACCOUNT:
                setField(field, forWho, context.getSystemService(Context.ACCOUNT_SERVICE));
                break;
            case ALARM:
                setField(field, forWho, context.getSystemService(Context.ALARM_SERVICE));
                break;
            case AM:
                setField(field, forWho, context.getSystemService(Context.ACTIVITY_SERVICE));
                break;
            case WM:
                setField(field, forWho, context.getSystemService(Context.WINDOW_SERVICE));
                break;
            case NM:
                setField(field, forWho, context.getSystemService(Context.NOTIFICATION_SERVICE));
                break;
            case TM:
                setField(field, forWho, context.getSystemService(Context.TELEPHONY_SERVICE));
                break;
            case TCM:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setField(field, forWho, context.getSystemService(Context.TELECOM_SERVICE));
                }
                break;
            case SP:
                setField(field, forWho, PreferenceManager.getDefaultSharedPreferences(context));
                break;
            case PKM:
                setField(field, forWho, context.getPackageManager());
                break;
            case HANDLE:
                setField(field, forWho, new Handler(Looper.getMainLooper()));
                break;
            case ASM:
                setField(field, forWho, context.getSystemService(Context.ACCESSIBILITY_SERVICE));
                break;
            case CAP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setField(field, forWho, context.getSystemService(Context.CAPTIONING_SERVICE));
                }
                break;
            case KGD:
                setField(field, forWho, context.getSystemService(Context.KEYGUARD_SERVICE));
                break;
            case LOCATION:
                setField(field, forWho, context.getSystemService(Context.LOCATION_SERVICE));
                break;
            case SEARCH:
                setField(field, forWho, context.getSystemService(Context.SEARCH_SERVICE));
                break;
            case SENSOR:
                setField(field, forWho, context.getSystemService(Context.SENSOR_SERVICE));
                break;
            case STORAGE:
                setField(field, forWho, context.getSystemService(Context.STORAGE_SERVICE));
                break;
            case WALLPAPER:
                setField(field, forWho, context.getSystemService(Context.WALLPAPER_SERVICE));
                break;
            case VIBRATOR:
                setField(field, forWho, context.getSystemService(Context.VIBRATOR_SERVICE));
                break;
            case CONNECT:
                setField(field, forWho, context.getSystemService(Context.CONNECTIVITY_SERVICE));
                break;
            case NETWORK_STATUS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setField(field, forWho, context.getSystemService(Context.NETWORK_STATS_SERVICE));
                }
                break;
            case WIFI:
                setField(field, forWho, context.getSystemService(Context.WIFI_SERVICE));
                break;
            case AUDIO:
                setField(field, forWho, context.getSystemService(Context.AUDIO_SERVICE));
                break;
            case FP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setField(field, forWho, context.getSystemService(Context.FINGERPRINT_SERVICE));
                }
                break;
            case MEDIA_ROUTER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setField(field, forWho, context.getSystemService(Context.MEDIA_ROUTER_SERVICE));
                }
                break;
            case SUB:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    setField(field, forWho, context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE));
                }
                break;
            case IME:
                setField(field, forWho, context.getSystemService(Context.INPUT_METHOD_SERVICE));
                break;
            case CLIP_BOARD:
                setField(field, forWho, context.getSystemService(Context.CLIPBOARD_SERVICE));
                break;
            case APP_WIDGET:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setField(field, forWho, context.getSystemService(Context.APPWIDGET_SERVICE));
                }
                break;
            case DEVICE_POLICY:
                setField(field, forWho, context.getSystemService(Context.DEVICE_POLICY_SERVICE));
                break;
            case DOWNLOAD:
                setField(field, forWho, context.getSystemService(Context.DOWNLOAD_SERVICE));
                break;
            case BATTERY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setField(field, forWho, context.getSystemService(Context.BATTERY_SERVICE));
                }
                break;
            case NFC:
                setField(field, forWho, context.getSystemService(Context.NFC_SERVICE));
                break;
            case DISPLAY:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    setField(field, forWho, context.getSystemService(Context.DISPLAY_SERVICE));
                }
                break;
            case USER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    setField(field, forWho, context.getSystemService(Context.USER_SERVICE));
                }
                break;
            case APP_OPS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    setField(field, forWho, context.getSystemService(Context.APP_OPS_SERVICE));
                }
                break;
        }
        logV("wired:" + type + ", for:" + forWho);
    }

    protected Findable.Type determineType(Class clz) {
        Findable.Type[] all = Findable.Type.values();
        for (Findable.Type t : all) {
            if (isTypeOf(clz, t.targetClass)) {
                return t;
            }
        }
        return null;
    }

    protected boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }
}

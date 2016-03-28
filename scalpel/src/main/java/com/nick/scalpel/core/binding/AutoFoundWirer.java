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

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsFieldWirer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.isBaseDataType;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

public class AutoFoundWirer extends AbsFieldWirer {

    public AutoFoundWirer(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return AutoFound.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        WireParam param = getParam(activity, field);
        if (param == null) return;
        switch (param.type) {
            case VIEW:
                wire(activity.getWindow().getDecorView(), activity, field);
                break;
            default:
                wire(activity.getApplicationContext(), activity, field);
                break;
        }
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        WireParam param = getParam(fragment, field);
        if (param == null) return;
        switch (param.type) {
            case VIEW:
                wire(fragment.getView(), fragment, field);
                break;
            default:
                wire(fragment.getActivity(), fragment, field);
                break;
        }
    }

    @Override
    public void wire(Service service, Field field) {
        wire(service.getApplicationContext(), service, field);
    }

    private void wireFromContext(Context context, AutoFound.Type type, int idRes, Resources.Theme theme, Field field, Object forWho) {
        Resources resources = context.getResources();
        switch (type) {
            case STRING:
                setField(field, forWho, resources.getString(idRes));
                break;
            case COLOR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setField(field, forWho, resources.getColor(idRes, theme));
                } else {
                    //noinspection deprecation
                    setField(field, forWho, resources.getColor(idRes));
                }
                break;
            case INTEGER:
                setField(field, forWho, resources.getInteger(idRes));
                break;
            case BOOL:
                setField(field, forWho, resources.getBoolean(idRes));
                break;
            case STRING_ARRAY:
                setField(field, forWho, resources.getStringArray(idRes));
                break;
            case INT_ARRAY:
                setField(field, forWho, resources.getIntArray(idRes));
                break;
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
            case BITMAP:
                setField(field, forWho, BitmapFactory.decodeResource(resources, idRes, null));
                break;
        }
    }

    protected WireParam getParam(Object object, Field field) {
        makeAccessible(field);

        Object fieldObject = getField(field, object);

        boolean isBaseType = false;

        if (fieldObject != null)
            isBaseType = isBaseDataType(fieldObject.getClass());
        if (fieldObject != null && !isBaseType) return null;

        AutoFound found = field.getAnnotation(AutoFound.class);
        AutoFound.Type type = found.type();

        if (type == AutoFound.Type.AUTO) {
            type = autoDetermineType(field.getType());
            if (type == null) return null;
            return new WireParam(type, found.id());
        } else if (isTypeOf(field.getType(), type.targetClass))
            return new WireParam(type, found.id());
        return null;
    }

    protected AutoFound.Type autoDetermineType(Class clz) {
        AutoFound.Type[] all = AutoFound.Type.values();
        int matchCnt = 0;
        AutoFound.Type found = null;
        for (AutoFound.Type t : all) {
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
        wireFromContext(context, param.type, param.idRes, null, field, object);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        WireParam param = getParam(object, field);
        if (param == null) return;
        switch (param.type) {
            case VIEW:
                setField(field, object, root.findViewById(param.idRes));
                break;
            default:
                wire(root.getContext(), object, field);
        }
    }

    protected boolean isTypeOf(Class clz, Class target) {
        if (clz == target) return true;
        Class sup = clz.getSuperclass();
        return sup != null && isTypeOf(sup, target);
    }

    class WireParam {
        AutoFound.Type type;
        int idRes;

        public WireParam(AutoFound.Type type, int idRes) {
            this.type = type;
            this.idRes = idRes;
        }

        @Override
        public String toString() {
            return "WireParam{" +
                    "type=" + type +
                    ", idRes=" + idRes +
                    '}';
        }
    }
}

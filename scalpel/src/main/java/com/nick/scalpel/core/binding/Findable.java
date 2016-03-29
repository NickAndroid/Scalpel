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

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.NetworkStatsManager;
import android.appwidget.AppWidgetManager;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;

interface Findable {

    enum Type { //TODO Add more possible types.

        AUTO(null),
        HANDLE(Handler.class),
        PM(PowerManager.class),
        PKM(PackageManager.class),
        WM(WindowManager.class),
        INFLATER(LayoutInflater.class),
        ACCOUNT(AccountManager.class),
        AM(ActivityManager.class),
        ASM(AccessibilityManager.class),
        CAP(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? CaptioningManager.class : NULL.class),
        KGD(KeyguardManager.class),
        LOCATION(LocationManager.class),
        SEARCH(SearchManager.class),
        SENSOR(SensorManager.class),
        STORAGE(StorageManager.class),
        WALLPAPER(WallpaperService.class),
        VIBRATOR(Vibrator.class),
        CONNECT(ConnectivityManager.class),
        NETWORK_STATUS(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? NetworkStatsManager.class : NULL.class),
        WIFI(WifiManager.class),
        AUDIO(AudioManager.class),
        FP(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? FingerprintManager.class : NULL.class),
        SUB(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ? SubscriptionManager.class : NULL.class),
        IME(InputMethodManager.class),
        CLIP_BOARD(ClipboardManager.class),
        MEDIA_ROUTER(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ? MediaRouter.class : NULL.class),
        APP_WIDGET(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? AppWidgetManager.class : NULL.class),
        DEVICE_POLICY(DevicePolicyManager.class),
        DOWNLOAD(DownloadManager.class),
        BATTERY(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? BatteryManager.class : NULL.class),
        NFC(NfcManager.class),
        DISPLAY(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? DisplayManager.class : NULL.class),
        USER(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? UserManager.class : NULL.class),
        APP_OPS(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? AppOpsManager.class : NULL.class),
        ALARM(AlarmManager.class),
        NM(NotificationManager.class),
        TM(TelephonyManager.class),
        TCM(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? TelecomManager.class : NULL.class),
        SP(SharedPreferences.class),
        VIEW(android.view.View.class),
        BITMAP(Bitmap.class),
        COLOR(int.class),
        STRING(String.class),
        BOOL(boolean.class),
        INTEGER(int.class),
        STRING_ARRAY(String[].class),
        INT_ARRAY(int[].class);

        @NonNull
        public Class targetClass;

        Type(@NonNull Class targetClass) {
            this.targetClass = targetClass;
        }
    }

    interface NULL {
    }
}


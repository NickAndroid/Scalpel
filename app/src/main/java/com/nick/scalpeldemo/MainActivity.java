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

package com.nick.scalpeldemo;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IPowerManager;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephonyListener;
import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.core.AutoBind;
import com.nick.scalpel.core.AutoFound;
import com.nick.scalpel.core.AutoRecycle;
import com.nick.scalpel.core.AutoRegister;
import com.nick.scalpel.core.AutoRequestFullScreen;
import com.nick.scalpel.core.AutoRequirePermission;
import com.nick.scalpel.core.AutoRequireRoot;
import com.nick.scalpel.core.OnClick;
import com.nick.scalpel.core.OnTouch;
import com.nick.scalpel.core.SystemService;
import com.nick.scalpel.core.os.Shell;

import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@AutoRequestFullScreen(viewToTriggerRestore = R.id.hello)
@AutoRequirePermission(requestCode = 100, permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE})
@AutoRequireRoot(mode = AutoRequireRoot.Mode.Async, callback = "this")
public class MainActivity extends ScalpelAutoActivity implements AutoBind.Callback, AutoRequireRoot.Callback {

    @AutoFound(id = R.id.toolbar, type = AutoFound.Type.VIEW)
    Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnTouch(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @AutoFound(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @AutoFound(id = R.integer.size, type = AutoFound.Type.INTEGER)
    int size;

    @AutoFound(id = R.color.colorAccent, type = AutoFound.Type.COLOR)
    int color;

    @AutoFound(id = R.string.app_name, type = AutoFound.Type.STRING)
    String text;

    @AutoFound(id = R.bool.boo, type = AutoFound.Type.BOOL)
    boolean bool;

    @AutoFound(id = R.array.strs, type = AutoFound.Type.STRING_ARRAY)
    String[] strs;

    @AutoFound(id = R.array.ints, type = AutoFound.Type.INT_ARRAY)
    int[] ints;

    @AutoFound
    PowerManager pm;

    @AutoFound
    TelephonyManager tm;

    @AutoFound
    NotificationManager nm;

    @AutoFound
    AccountManager accountManager;

    @AutoFound
    ActivityManager am;

    @AutoFound
    AlarmManager alarmManager;

    @AutoBind(action = "com.nick.service", pkg = "com.nick.scalpeldemo", callback = "this"
            , autoUnbind = true)
    IMyAidlInterface mService;

    @AutoRegister(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = true)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @AutoFound(id = R.drawable.bitmap)
    @AutoRecycle
    Bitmap bitmap;

    @SystemService
    IPowerManager powerManager;

    @SystemService
    ITelephony telephony;

    private View.OnClickListener mokeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own actions", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    private View.OnTouchListener mokeTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Snackbar.make(v, "Replace with your own actions", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
    };

    private AutoBind.Callback mCallback = new AutoBind.Callback() {
        @Override
        public void onServiceBound(ComponentName name, ServiceConnection connection, Intent intent) {
            Log.d("Scalpel.Demo", "onServiceBound, service = " + mService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Scalpel.Demo", "onServiceDisconnected, service = " + mService);
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        hello.setTextSize(size);
        hello.setTextColor(color);
        hello.setText(text + "-" + bool + "-" + Arrays.toString(strs) + "-" + Arrays.toString(ints));

        new ViewHolder(this);

        log(toolbar, fab, hello, size, color, text, bool, strs, ints, am, pm, tm, nm, accountManager, alarmManager);

        // getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyFragment()).commit();

        log(bitmap);
        log(mService);

        log(powerManager);
        log(telephony);

        try {
            telephony.setRadioPower(false);
            telephony.addListener(new ITelephonyListener.Stub() {
                @Override
                public void onUpdate(int callId, int state, String number) throws RemoteException {
                    log("onUpdate:" + number + ", state:" + state);
                }
            });
        } catch (RemoteException e) {

        } catch (SecurityException se) {
            se.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                log(mService);
            }
        }, 3000);
    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getName(), String.valueOf(o));
        }
    }

    public void showSnack(String content, String owner) {
        Snackbar.make(getWindow().getDecorView(), owner + ": " + content, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onServiceBound(ComponentName name, ServiceConnection connection, Intent intent) {
        mCallback.onServiceBound(name, connection, intent);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mCallback.onServiceDisconnected(name);
    }

    @Override
    public void onRootResult(boolean hasRoot, @Nullable Shell shell) {
        log("onRootResult:" + hasRoot + ", shell = " + shell);
        if (shell != null) shell.exec("reboot", new Shell.FeedbackReceiver() {
            @Override
            public boolean onFeedback(String feedback) {
                log("onFeedback:" + feedback);
                return false;
            }
        });
    }
}

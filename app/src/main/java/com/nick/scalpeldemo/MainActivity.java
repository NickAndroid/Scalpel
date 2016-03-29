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
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.annotation.binding.AutoWired;
import com.nick.scalpel.annotation.binding.BindService;
import com.nick.scalpel.annotation.binding.FindBitmap;
import com.nick.scalpel.annotation.binding.FindBool;
import com.nick.scalpel.annotation.binding.FindColor;
import com.nick.scalpel.annotation.binding.FindInt;
import com.nick.scalpel.annotation.binding.FindIntArray;
import com.nick.scalpel.annotation.binding.FindString;
import com.nick.scalpel.annotation.binding.FindStringArray;
import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.annotation.binding.OnClick;
import com.nick.scalpel.annotation.binding.OnTouch;
import com.nick.scalpel.annotation.binding.RegisterReceiver;
import com.nick.scalpel.annotation.hook.RequireRoot;
import com.nick.scalpel.annotation.opt.AutoRecycle;
import com.nick.scalpel.annotation.opt.RetrieveBean;
import com.nick.scalpel.annotation.request.RequestFullScreen;
import com.nick.scalpel.annotation.request.RequirePermission;
import com.nick.scalpel.core.hook.Shell;
import com.nick.scalpel.core.opt.RecyclerManager;

import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@RequestFullScreen(viewToTriggerRestore = R.id.hello)
@RequirePermission(requestCode = 100, permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE})
@RequireRoot(mode = RequireRoot.Mode.Async, callback = "this")
public class MainActivity extends ScalpelAutoActivity implements BindService.Callback, RequireRoot.Callback {

    @FindView(id = R.id.toolbar)
    Toolbar toolbar;

    @FindView(id = R.id.fab)
    @OnTouch(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @FindView(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @FindInt(id = R.integer.size)
    int size;

    @FindColor(id = R.color.colorAccent)
    int color;

    @FindString(id = R.string.app_name)
    String text;

    @FindBool(id = R.bool.boo)
    boolean bool;

    @FindStringArray(id = R.array.strs)
    String[] strs;

    @FindIntArray(id = R.array.ints)
    int[] ints;

    @AutoWired
    PowerManager pm;

    @AutoWired
    TelephonyManager tm;

    @AutoWired
    NotificationManager nm;

    @AutoWired
    AccountManager accountManager;

    @AutoWired
    ActivityManager am;

    @AutoWired
    AlarmManager alarmManager;

    @BindService(action = "com.nick.service", pkg = "com.nick.scalpeldemo", callback = "this"
            , autoUnbind = true)
    IMyAidlInterface mService;

    @RegisterReceiver(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = true)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @FindBitmap(idRes = R.drawable.bitmap)
    @AutoRecycle
    Bitmap bitmap;

    @Custom
    Object custom;

    @RetrieveBean
    EmptyConObject emptyConObject;

    @RetrieveBean
    ContextConsObject contextConsObject;

    @RetrieveBean
    ContextConsObject contextConsObject2;

    @RetrieveBean(id = R.id.context_obj)
    ContextConsObject contextConsObjectStrict;

    @RetrieveBean
    RecyclerManager mRecyclerManager;

    @AutoWired
    StorageManager sManager;

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

    private BindService.Callback mCallback = new BindService.Callback() {
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

        log(emptyConObject, contextConsObject, contextConsObject2, contextConsObjectStrict);


    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getName(), (o == null ? "null" : o.getClass().getSimpleName()) + "-" + String.valueOf(o));
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerManager.recycle(this);
    }
}

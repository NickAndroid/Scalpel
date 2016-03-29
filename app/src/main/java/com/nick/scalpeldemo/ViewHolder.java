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

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.PowerManager;
import android.os.storage.StorageManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.annotation.binding.AutoWired;
import com.nick.scalpel.annotation.binding.BindService;
import com.nick.scalpel.annotation.binding.BindService.Callback;
import com.nick.scalpel.annotation.binding.FindBitmap;
import com.nick.scalpel.annotation.binding.FindBool;
import com.nick.scalpel.annotation.binding.FindColor;
import com.nick.scalpel.annotation.binding.FindInt;
import com.nick.scalpel.annotation.binding.FindIntArray;
import com.nick.scalpel.annotation.binding.FindString;
import com.nick.scalpel.annotation.binding.FindStringArray;
import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.annotation.binding.OnClick;
import com.nick.scalpel.annotation.binding.RegisterReceiver;
import com.nick.scalpel.annotation.opt.RetrieveBean;
import com.nick.scalpel.core.opt.RecyclerManager;

public class ViewHolder implements Callback{
    @FindView(id = R.id.toolbar)
    Toolbar toolbar;

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
            , autoUnbind = false)
    IMyAidlInterface mService;

    @RegisterReceiver(actions = {Intent.ACTION_SCREEN_ON, Intent.ACTION_SCREEN_OFF, "com.nick.service.bind"}
            , autoUnRegister = false)
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Scalpel.Demo", "onReceive, intent = " + intent.getAction());
        }
    };

    @FindBitmap(idRes = R.drawable.bitmap)
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

    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getInstance().wire(rootV, this);

        log(toolbar, hello, size, color, text, bool, strs, ints, am, pm, tm, nm, accountManager, alarmManager);
    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getSimpleName(), o.toString());
        }
    }

    @Override
    public void onServiceBound(ComponentName name, ServiceConnection connection, Intent intent) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}

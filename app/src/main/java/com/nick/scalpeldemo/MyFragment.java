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
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nick.scalpel.ScalpelAutoFragment;
import com.nick.scalpel.core.AutoFound;
import com.nick.scalpel.core.OnClick;
import com.nick.scalpel.core.AutoFoundType;

public class MyFragment extends ScalpelAutoFragment {

    @AutoFound(id = R.id.toolbar, type = AutoFoundType.View)
    Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @AutoFound(id = R.id.hello)
    TextView hello;

    @AutoFound(id = R.integer.size, type = AutoFoundType.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = AutoFoundType.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = AutoFoundType.String)
    String text;

    @AutoFound(id = R.bool.boo, type = AutoFoundType.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs, type = AutoFoundType.StringArray)
    String[] strs;

    @AutoFound(id = R.array.ints, type = AutoFoundType.IntArray)
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log(toolbar, fab, hello, size, color, text, bool, strs, ints, am, pm, tm, nm, accountManager, alarmManager);
    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getSimpleName(), String.valueOf(o));
        }
    }

    public void showSnack(String content, String owner) {
        Snackbar.make(getView(), owner + ": " + content, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}

package com.nick.scalpeldemo;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.intarnal.AutoFound;
import com.nick.scalpel.intarnal.OnClick;
import com.nick.scalpel.intarnal.Type;

public class ViewHolder {
    @AutoFound(id = R.id.toolbar) // Same as @AutoFound(id = R.id.toolbar, type = Type.Auto)
            Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(listener = "mokeListener")
    FloatingActionButton fab;

    @OnClick(listener = "mokeListener")
    @AutoFound(id = R.id.hello)
    TextView hello;

    @AutoFound(id = R.integer.size, type = Type.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = Type.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = Type.String)
    String text;

    @AutoFound(id = R.bool.boo, type = Type.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs)
    String[] strs;

    @AutoFound(id = R.array.ints, type = Type.Auto)
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

    private View.OnClickListener mokeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getDefault().wire(rootV, this);
        Scalpel.getDefault().wire(context, this);

        log(toolbar, fab, hello, size, color, text, bool, strs, ints, am, pm, tm, nm, accountManager, alarmManager);
    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getSimpleName(), o.toString());
        }
    }
}

package com.nick.scalpeldemo;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.intarnal.AutoFound;
import com.nick.scalpel.intarnal.Type;

public class ViewHolder {
    @AutoFound(id = R.id.toolbar) // Same as @AutoFound(id = R.id.toolbar, type = Type.Auto)
            Toolbar toolbar;
    @AutoFound(id = R.id.fab)
    FloatingActionButton fab;
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

    ViewHolder(Context context) {
        View rootV = LayoutInflater.from(context).inflate(R.layout.activity_main, null);
        Scalpel.getDefault().wire(rootV, this);
        Scalpel.getDefault().wire(context, this);

        log(toolbar, fab, hello, size, color, text, bool, strs, ints);
    }

    void log(Object... os) {
        for (Object o : os) {
            Log.d(getClass().getSimpleName(), o.toString());
        }
    }
}

package com.nick.scalpeldemo;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.intarnal.AutoFound;
import com.nick.scalpel.intarnal.OnClick;
import com.nick.scalpel.intarnal.Type;

import java.util.Arrays;

public class MainActivity extends ScalpelAutoActivity {

    @AutoFound(id = R.id.toolbar, type = Type.View)
    Toolbar toolbar;

    @AutoFound(id = R.id.fab)
    @OnClick(action = "showSnack", args = {"Hello, I am a fab!", "Nick"})
    FloatingActionButton fab;

    @AutoFound(id = R.id.hello)
    @OnClick(listener = "mokeListener")
    TextView hello;

    @AutoFound(id = R.integer.size, type = Type.Integer)
    int size;

    @AutoFound(id = R.color.colorAccent, type = Type.Color)
    int color;

    @AutoFound(id = R.string.app_name, type = Type.String)
    String text;

    @AutoFound(id = R.bool.boo, type = Type.Bool)
    boolean bool;

    @AutoFound(id = R.array.strs, type = Type.StringArray)
    String[] strs;

    @AutoFound(id = R.array.ints, type = Type.IntArray)
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

        Log.d("Scalpel.Demo", "pm = " + pm);
        Log.d("Scalpel.Demo", "tm = " + tm);
        Log.d("Scalpel.Demo", "nm = " + nm);
        Log.d("Scalpel.Demo", "accountManager = " + accountManager);
        Log.d("Scalpel.Demo", "am = " + am);
        Log.d("Scalpel.Demo", "alarmManager = " + alarmManager);
    }

    public void showSnack(String content, String owner) {
        Snackbar.make(getWindow().getDecorView(), owner + ": " + content, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

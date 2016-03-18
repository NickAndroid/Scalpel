package com.nick.scalpeldemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.intarnal.AutoFound;
import com.nick.scalpel.intarnal.Type;

import java.util.Arrays;

public class MainActivity extends ScalpelAutoActivity {

    @AutoFound(id = R.id.toolbar)
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
    @AutoFound(id = R.array.strs, type = Type.StringArray)
    String[] strs;
    @AutoFound(id = R.array.ints, type = Type.IntArray)
    int[] ints;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        hello.setTextSize(size);
        hello.setTextColor(color);
        hello.setText(text + "-" + bool + "-" + Arrays.toString(strs) + "-" + Arrays.toString(ints));

        new ViewHolder(this);
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

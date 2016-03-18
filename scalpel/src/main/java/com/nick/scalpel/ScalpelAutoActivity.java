package com.nick.scalpel;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class ScalpelAutoActivity extends AppCompatActivity {
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Scalpel.getDefault().wire(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        Scalpel.getDefault().wire(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        Scalpel.getDefault().wire(this);
    }
}

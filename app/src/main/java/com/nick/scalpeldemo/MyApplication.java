package com.nick.scalpeldemo;

import android.app.Application;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.config.Configuration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Scalpel.getDefault().config(Configuration.builder().autoFindIfNull(true).debug(true).logTag("Scalpel").build());
    }
}

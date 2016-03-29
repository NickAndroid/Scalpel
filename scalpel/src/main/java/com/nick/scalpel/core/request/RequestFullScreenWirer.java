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

package com.nick.scalpel.core.request;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.nick.scalpel.annotation.opt.Beta;
import com.nick.scalpel.annotation.request.RequestFullScreen;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsClassWirer;
import com.nick.scalpel.core.utils.Preconditions;

import java.lang.annotation.Annotation;

@Beta
class RequestFullScreenWirer extends AbsClassWirer {

    private Handler mHandler;

    public RequestFullScreenWirer(Configuration configuration, Handler handler) {
        super(configuration);
        mHandler = handler;
    }

    @Override
    public void wire(final Object o) {
        Preconditions.checkState(o instanceof Activity);

        final RequestFullScreen autoRequestFullScreen = o.getClass().getAnnotation(RequestFullScreen.class);
        final boolean showStatus = autoRequestFullScreen.keepStatusBar();
        final boolean showAppBar = autoRequestFullScreen.keepAppBar();
        if (showAppBar && showStatus) return;

        long delayMills = autoRequestFullScreen.delayMills();

        Runnable hideRunnable = new Runnable() {
            @Override
            public void run() {
                Activity activity = (Activity) o;
                final View decor = activity.getWindow().getDecorView();

                if (!showStatus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                    }
                }
                if (!showAppBar) {
                    if (activity instanceof AppCompatActivity) {
                        AppCompatActivity compatActivity = (AppCompatActivity) activity;
                        if (compatActivity.getSupportActionBar() != null) {
                            compatActivity.getSupportActionBar().hide();
                        }
                    } else {
                        if (activity.getActionBar() != null) activity.getActionBar().hide();
                    }
                }
                int triggerId = autoRequestFullScreen.viewToTriggerRestore();
                if (triggerId <= 0) return;
                if (!showStatus) {
                    View v = decor.findViewById(triggerId);
                    v.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                            }
                            return false;
                        }
                    });
                }
                if (!showAppBar) {
                    if (activity instanceof AppCompatActivity) {
                        AppCompatActivity compatActivity = (AppCompatActivity) activity;
                        if (compatActivity.getSupportActionBar() != null) {
                            compatActivity.getSupportActionBar().show();
                        }
                    } else {
                        if (activity.getActionBar() != null) activity.getActionBar().show();
                    }
                }
            }
        };
        mHandler.postDelayed(hideRunnable, delayMills);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RequestFullScreen.class;
    }
}

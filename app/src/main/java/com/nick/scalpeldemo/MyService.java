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

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nick.scalpel.ScalpelAutoService;
import com.nick.scalpel.annotation.binding.MainThreadHandler;
import com.nick.scalpel.annotation.binding.WorkerThreadHandler;
import com.nick.scalpel.core.binding.ThisThatNull;

public class MyService extends ScalpelAutoService implements Handler.Callback {

    @MainThreadHandler
    private Handler mMainHandler;

    @WorkerThreadHandler(callback = ThisThatNull.THIS)
    Handler mWorkHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sendBroadcast(new Intent("com.nick.service.bind"));
        testHandler();
        return new MyStub();
    }

    public void testHandler() {
        Log.d("ScalpelAutoService", "mMainHandler: " + mMainHandler.getLooper().getThread().getName());
        Log.d("ScalpelAutoService", "mWorkHandler: " + mWorkHandler.getLooper().getThread().getName());
        mWorkHandler.sendEmptyMessage(1000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d("ScalpelAutoService", "handleMessage:" + msg.what);
        return false;
    }

    class MyStub extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {
        }
    }
}

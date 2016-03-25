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
import android.os.IBinder;
import android.os.IPowerManager;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.nick.commands.sca.IScaService;
import com.nick.scalpel.ScalpelAutoService;
import com.nick.scalpel.core.AutoFound;
import com.nick.scalpel.core.SystemService;

public class MyService extends ScalpelAutoService {

    @AutoFound
    PowerManager pm;
    @SystemService
    IScaService scaService;

    @SystemService
    ITelephony telephony;

    @SystemService
    IPowerManager manager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Scalpel.MyService", "pm = " + pm);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Scalpel.MyService", "pm = " + pm);
        sendBroadcast(new Intent("com.nick.service.bind"));

        if (scaService != null) try {
            scaService.run();
        } catch (RemoteException ignored) {

        }

        if (telephony != null) {
            try {
                telephony.setRadio(!telephony.isRadioOn());
            } catch (RemoteException ignored) {

            }
        }

        if (manager != null) {
            try {
                manager.setPowerSaveMode(true);
            } catch (RemoteException ignored) {

            }
        }

        return new MyStub();
    }

    class MyStub extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {
        }
    }
}

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
package com.nick.commands.sca;

import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.telephony.ITelephony;

public class ScaHookService extends com.nick.commands.sca.IScaService.Stub {

    static ScaHookService me = new ScaHookService();

    private ScaHookService() {
        try {
            asBinder().linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    System.err.println("Sca service die.");
                }
            }, 0);
        } catch (RemoteException ignored) {

        }
    }

    public static ScaHookService get() {
        return me;
    }

    void start() {
        Sca.log("Start.");
    }

    @Override
    public void run() throws RemoteException {
        Sca.log("Sca running!");
    }

    @Override
    public void setRadioPower(boolean on) throws RemoteException {
        Binder.clearCallingIdentity();
        ITelephony telephony = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        telephony.setRadioPower(on);
    }
}

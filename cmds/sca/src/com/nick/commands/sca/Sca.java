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

import android.os.IBinder;
import android.os.IPowerManager;
import android.os.PowerManagerProxy;
import android.os.ServiceManager;
import android.util.Slog;

import com.android.internal.os.BaseCommand;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.TelephonyManagerProxy;

import java.io.PrintStream;

public class Sca extends BaseCommand {

    static final String TAG = "Scalpel.HookService";

    @Override
    public void onRun() throws Exception {

        String arg = nextArgRequired();
        int req;

        try {
            req = Integer.parseInt(arg);
        } catch (Exception e) {
            sent(new Feedback(Response.BAD_REQUEST, "Invalid arg:" + arg));
            return;
        }

        switch (req) {
            case Request.START_SERVICE:
                doStart();
                break;
            case Request.STOP_SERVICE:
                doStop();
                break;
        }

        log("onRun complete, process will be terminated!");
    }

    private void doStart() {
        IBinder scaBinder = ServiceManager.getService("sca");

        log("scaBinder:" + scaBinder);

        if (scaBinder != null) {
            sent(new Feedback(Response.START_FAILURE_ALREADY_STARTED, "Sca server already started."));
            return;
        }

        ServiceManager.addService(ScaContext.SCA_SERVICE, ScaHookService.get(), true);
        ServiceManager.addService(ScaContext.SCA_TELEPHONY_SERVICE, new TelephonyManagerProxy(), true);
        ServiceManager.addService(ScaContext.SCA_POWER_SERVICE, new PowerManagerProxy().asBinder(), true);

        com.nick.commands.sca.IScaService me =
                com.nick.commands.sca.IScaService.Stub.asInterface(ServiceManager.getService(ScaContext.SCA_SERVICE));
        ITelephony telephony = ITelephony.Stub.asInterface(ServiceManager.getService(ScaContext.SCA_TELEPHONY_SERVICE));
        IPowerManager power = IPowerManager.Stub.asInterface(ServiceManager.getService(ScaContext.SCA_POWER_SERVICE));

        log("Sca service:" + me);
        log("Sca phone service:" + telephony);
        log("Sca power service:" + power);

        if (me == null) {
            sent(new Feedback(Response.START_FAILURE_SYSTEM_ERR, "Sca server startup failure, have you installed?"));
            return;
        }

        sent(new Feedback(Response.START_OK, "Sca server startup success."));
        ServiceKeeper keeper = new ServiceKeeper();
        keeper.keep();
    }

    private void doStop() {
        IBinder scaBinder = ServiceManager.getService("sca");

        if (scaBinder == null) {
            sent(new Feedback(Response.STOP_FAILURE_NOT_STARTED, "Sca server not started."));
        }
    }

    @Override
    public void onShowUsage(PrintStream out) {
        out.println(
                "usage: sca [subcommand]\n");
    }

    private void sent(Object content) {
        System.err.println(String.valueOf(content));
        log("sent:" + String.valueOf(content));
    }

    public static void log(Object content) {
        Slog.d(TAG, String.valueOf(content));
    }

    public static void main(String[] args) {
        (new Sca()).run(args);
    }
}

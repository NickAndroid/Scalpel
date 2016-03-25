package com.nick.commands.sca;

import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Slog;

import com.android.internal.os.BaseCommand;

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

        ServiceManager.addService("sca", ScaService.get(), true);
        com.nick.commands.sca.IScaService me =
                com.nick.commands.sca.IScaService.Stub.asInterface(ServiceManager.getService("sca"));

        log("Sca service:" + me);

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

    static void log(Object content) {
        Slog.d(TAG, String.valueOf(content));
    }

    public static void main(String[] args) {
        (new Sca()).run(args);
    }
}

package com.nick.commands.sca;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ServiceManager;

import com.android.internal.os.BaseCommand;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Sca extends BaseCommand {

    public static void main(String[] args) {
        (new Sca()).run(args);
    }

    @Override
    public void onRun() throws Exception {

        IBinder iBinder = ServiceManager.getService("sca");

        System.err.println("ibinder;" + iBinder);

        ServiceManager.addService("sca", ScaService.get(), true);
        com.nick.commands.sca.IScaService me =
                com.nick.commands.sca.IScaService.Stub.asInterface(ServiceManager.getService("sca"));
        System.err.println("me: " + me);

        com.nick.commands.sca.IScaService me2 =
                com.nick.commands.sca.IScaService.Stub.asInterface(ServiceManager.getService("sca"));
        System.err.println("me2: " + me2);

        System.err.println(Arrays.toString(ServiceManager.listServices()));

        new CountDownLatch(1).await();
    }

    @Override
    public void onShowUsage(PrintStream out) {
        out.println(
                "usage: sca [subcommand] [options]\n");
    }
}

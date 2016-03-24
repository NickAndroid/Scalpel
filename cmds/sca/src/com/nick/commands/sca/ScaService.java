package com.nick.commands.sca;

import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.telephony.ITelephony;

public class ScaService extends com.nick.commands.sca.IScaService.Stub {

    static ScaService me = new ScaService();

    private ScaService() {
        linkToDeath(new DeathRecipient() {
            @Override
            public void binderDied() {
                System.err.println("Sca service die.");
            }
        }, 0);
    }

    public static ScaService get() {
        return me;
    }

    void start() {
        System.err.println("Sca, start!");
    }

    @Override
    public void run() throws RemoteException {
        System.err.println("Hi, Nick!");
    }

    @Override
    public void setRadioPower(boolean on) throws RemoteException {
        System.err.println("O, Calling uid:" + Binder.getCallingUid());
        Binder.clearCallingIdentity();
        System.err.println("Calling uid:" + Binder.getCallingUid());
        ITelephony telephony = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
        telephony.setRadioPower(on);
    }
}

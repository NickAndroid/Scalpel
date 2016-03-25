package com.nick.commands.sca;

import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.telephony.ITelephony;

public class ScaService extends com.nick.commands.sca.IScaService.Stub {

    static ScaService me = new ScaService();

    private ScaService() {
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

    public static ScaService get() {
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

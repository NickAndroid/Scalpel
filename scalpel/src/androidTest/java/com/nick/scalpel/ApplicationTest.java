package com.nick.scalpel;

import android.app.Application;
import android.os.IBinder;
import android.os.IPowerManager;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.nick.scalpel.core.os.DroidServiceManager;
import com.nick.scalpel.core.os.ServiceType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        IBinder binder = new DroidServiceManager().getServiceBinder(ServiceType.POWER);

        IPowerManager manager = IPowerManager.Stub.asInterface(binder);
        Log.d("Scalpel.Test", "manager:" + manager);
    }
}
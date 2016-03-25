package com.nick.commands.sca;

import java.util.concurrent.CountDownLatch;

class ServiceKeeper {

    private CountDownLatch mAwaitor;

    ServiceKeeper() {
        mAwaitor = new CountDownLatch(1);
    }

    void keep() {
        while (true) {
            try {
                mAwaitor.await();
                return;
            } catch (InterruptedException ignored) {

            }
        }
    }

    void release() {
        mAwaitor.countDown();
        System.err.println("ServiceKeeper, released.");
    }

}

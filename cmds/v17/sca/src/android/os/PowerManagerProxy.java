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
package android.os;

import com.nick.commands.sca.Sca;
import com.nick.commands.sca.ScaContext;

import java.io.FileDescriptor;
import java.io.PrintWriter;

public class PowerManagerProxy implements IInterface {

    IPowerManager mService;

    public PowerManagerProxy() {
        mService = IPowerManager.Stub.asInterface(ServiceManager.getService(ScaContext.POWER_SERVICE));
        Sca.log("PowerManagerProxy startup, mService=" + mService);
    }

    @Override
    public IBinder asBinder() {
        return new BinderService();
    }

    private final class BinderService extends IPowerManager.Stub {
        @Override // Binder call
        public void acquireWakeLockWithUid(IBinder lock, int flags, String tag,
                                           String packageName, int uid) throws RemoteException {
        }

        @Override // Binder call
        public void powerHint(int hintId, int data) throws RemoteException {

        }

        @Override // Binder call
        public void acquireWakeLock(IBinder lock, int flags, String tag, String packageName,
                                    WorkSource ws, String historyTag) throws RemoteException {
        }

        @Override // Binder call
        public void releaseWakeLock(IBinder lock, int flags) throws RemoteException {

        }

        @Override // Binder call
        public void updateWakeLockUids(IBinder lock, int[] uids) throws RemoteException {

        }

        @Override // Binder call
        public void updateWakeLockWorkSource(IBinder lock, WorkSource ws, String historyTag) throws RemoteException {

        }

        @Override // Binder call
        public boolean isWakeLockLevelSupported(int level) throws RemoteException {
            return false;
        }

        @Override // Binder call
        public void userActivity(long eventTime, int event, int flags) throws RemoteException {

        }

        @Override // Binder call
        public void wakeUp(long eventTime) throws RemoteException {

        }

        @Override // Binder call
        public void goToSleep(long eventTime, int reason, int flags) throws RemoteException {

        }

        @Override // Binder call
        public void nap(long eventTime) throws RemoteException {

        }

        @Override // Binder call
        public boolean isInteractive() throws RemoteException {
            return mService.isInteractive();
        }

        @Override // Binder call
        public boolean isPowerSaveMode() throws RemoteException {
            return mService.isPowerSaveMode();
        }

        @Override // Binder call
        public boolean setPowerSaveMode(boolean mode) throws RemoteException {
            Sca.log("setPowerSaveMode:" + mode + ", calling uid:" + Binder.getCallingUid());
            Binder.clearCallingIdentity();
            return mService.setPowerSaveMode(mode);
        }

        /**
         * Reboots the device.
         *
         * @param confirm If true, shows a reboot confirmation dialog.
         * @param reason  The reason for the reboot, or null if none.
         * @param wait    If true, this call waits for the reboot to complete and does not return.
         */
        @Override // Binder call
        public void reboot(boolean confirm, String reason, boolean wait) throws RemoteException {
            mService.reboot(confirm, reason, wait);
        }

        /**
         * Shuts down the device.
         *
         * @param confirm If true, shows a shutdown confirmation dialog.
         * @param wait    If true, this call waits for the shutdown to complete and does not return.
         */
        @Override // Binder call
        public void shutdown(boolean confirm, boolean wait) throws RemoteException {

        }

        /**
         * Crash the runtime (causing a complete restart of the Android framework).
         * Requires REBOOT permission.  Mostly for testing.  Should not return.
         */
        @Override // Binder call
        public void crash(String message) throws RemoteException {

        }

        /**
         * Set the setting that determines whether the device stays on when plugged in.
         * The argument is a bit string, with each bit specifying a power source that,
         * when the device is connected to that source, causes the device to stay on.
         * See {@link android.os.BatteryManager} for the list of power sources that
         * can be specified. Current values include
         * {@link android.os.BatteryManager#BATTERY_PLUGGED_AC}
         * and {@link android.os.BatteryManager#BATTERY_PLUGGED_USB}
         * <p/>
         * Used by "adb shell svc power stayon ..."
         *
         * @param val an {@code int} containing the bits that specify which power sources
         *            should cause the device to stay on.
         */
        @Override // Binder call
        public void setStayOnSetting(int val) throws RemoteException {

        }

        /**
         * Used by the settings application and brightness control widgets to
         * temporarily override the current screen brightness setting so that the
         * user can observe the effect of an intended settings change without applying
         * it immediately.
         * <p/>
         * The override will be canceled when the setting value is next updated.
         *
         * @param brightness The overridden brightness.
         * @see android.provider.Settings.System#SCREEN_BRIGHTNESS
         */
        @Override // Binder call
        public void setTemporaryScreenBrightnessSettingOverride(int brightness) throws RemoteException {

        }

        /**
         * Used by the settings application and brightness control widgets to
         * temporarily override the current screen auto-brightness adjustment setting so that the
         * user can observe the effect of an intended settings change without applying
         * it immediately.
         * <p/>
         * The override will be canceled when the setting value is next updated.
         *
         * @param adj The overridden brightness, or Float.NaN to disable the override.
         * @see android.provider.Settings.System#SCREEN_AUTO_BRIGHTNESS_ADJ
         */
        @Override // Binder call
        public void setTemporaryScreenAutoBrightnessAdjustmentSettingOverride(float adj) throws RemoteException {

        }

        /**
         * Used by the phone application to make the attention LED flash when ringing.
         */
        @Override // Binder call
        public void setAttentionLight(boolean on, int color) throws RemoteException {

        }

        @Override // Binder call
        public void boostScreenBrightness(long eventTime) throws RemoteException {

        }

        @Override // Binder call
        protected void dump(FileDescriptor fd, PrintWriter pw, String[] args) {

        }

        /* updates the blocked uids, so if a wake lock is acquired for it
         * can be released.
         */
        public void updateBlockedUids(int uid, boolean isBlocked) throws RemoteException {

        }
    }
}

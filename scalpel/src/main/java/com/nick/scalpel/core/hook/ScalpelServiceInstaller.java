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

package com.nick.scalpel.core.hook;

import android.text.TextUtils;
import android.util.Log;

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;
import com.nick.scalpel.core.utils.Preconditions;

import java.io.File;

public abstract class ScalpelServiceInstaller {

    static final String TAG = "Scalpel.Installer";

    public boolean install() throws Exception {

        Preconditions.checkState(checkPath(getJarPath()), "Invalid jar path:" + getJarPath());
        Preconditions.checkState(checkPath(getBinaryPath()), "Invalid bin path:" + getBinaryPath());

        boolean hasRoot = RootManager.getInstance().obtainPermission();
        if (!hasRoot) {
            Log.e(TAG, "No root access!");
            return false;
        }

        Result result = RootManager.getInstance().runCommand("cp " + getJarPath() + " system/framework/");
        boolean jarInstalled = result.getResult();
        if (!jarInstalled) {
            Log.e(TAG, "Failed to install jar:" + result.getMessage());
            return false;
        }

        result = RootManager.getInstance().runCommand("cp " + getBinaryPath() + " system/bin/");
        boolean binInstalled = result.getResult();
        if (!binInstalled) {
            Log.e(TAG, "Failed to install bin:" + result.getMessage());
            return false;
        }

//        boolean remount = RootManager.getInstance().remount("system", "rw");
//        if (!remount) {
//            Log.e(TAG, "Failed to remount system/bin");
//            return false;
//        }

        result = RootManager.getInstance().runCommand("chmod a+x system/bin/sca");
        boolean binExecutable = result.getResult();
        Log.d(TAG, "chmod message:" + result.getMessage());
        if (!binExecutable) {
            Log.e(TAG, "Failed to chmod bin:" + result.getMessage());
            return false;
        }

        Log.d(TAG, "ScalpelService installed!");
        return true;
    }

    private boolean checkPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }

    protected String getJarPath() {
        return null;
    }

    protected String getBinaryPath() {
        return null;
    }
}

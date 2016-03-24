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

package com.nick.scalpel.core.os;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class DroidRootRequester implements RootRequester {

    static final String TAG = "RootRequester";
    private boolean mHasRootAccess = false;

    @Override
    public boolean requestRoot() {
        return mHasRootAccess || execRootCommand("id", new FeedbackReceiver() {
            @Override
            public boolean onFeedback(String feedback) {
                boolean hasRoot = (!TextUtils.isEmpty(feedback) && feedback.contains("uid=0(root)"));
                mHasRootAccess = hasRoot;
                Log.d(TAG, "onFeedback:" + feedback + ", has root? " + hasRoot);
                return hasRoot;
            }
        }) && mHasRootAccess;

    }

    public boolean execRootCommand(String command) {
        return execRootCommand(command, null);
    }

    public boolean execRootCommand(String command, FeedbackReceiver receiver) {
        Process process = null;
        DataOutputStream os = null;
        DataInputStream in;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            in = new DataInputStream(process.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            if (receiver != null && receiver.onFeedback(builder.toString())) {
                return true;
            }
            process.waitFor();
        } catch (Exception e) {
            Log.e(TAG, "su root - the device is not rooted, error message： " + e.getMessage());
            return false;
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != process) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    interface FeedbackReceiver {
        boolean onFeedback(String feedback);
    }
}

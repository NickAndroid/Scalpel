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

class DroidRootRequester implements RootRequester {

    static final String TAG = "RootRequester";
    private boolean mHasRootAccess = false;
    private Shell mShell;

    @Override
    public boolean requestRoot() {
        if (mShell == null) mShell = new Shell();
        return mHasRootAccess || mShell.exec("id", new Shell.FeedbackReceiver() {
            @Override
            public boolean onFeedback(String feedback) {
                boolean hasRoot = (!TextUtils.isEmpty(feedback) && feedback.contains("uid=0(root)"));
                mHasRootAccess = hasRoot;
                Log.d(TAG, "onFeedback:" + feedback + ", has root? " + hasRoot);
                return hasRoot;
            }
        }) && mHasRootAccess;
    }

    public Shell getShell() {
        return mShell;
    }
}

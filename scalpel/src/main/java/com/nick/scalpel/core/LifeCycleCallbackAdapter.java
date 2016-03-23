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

package com.nick.scalpel.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class LifeCycleCallbackAdapter implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // Noop
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // Noop
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // Noop
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // Noop
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // Noop
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // Noop
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // Noop
    }
}

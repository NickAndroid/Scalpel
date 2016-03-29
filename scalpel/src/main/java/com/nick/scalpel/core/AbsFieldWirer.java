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

import android.util.Log;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.config.Configuration;

public abstract class AbsFieldWirer implements FieldWirer, Publishable {

    static final String LOG_TAG_CONNECTOR = ".";

    private boolean debug;
    private String logTag;

    public AbsFieldWirer(Configuration configuration) {
        this.debug = configuration.isDebug();
        this.logTag = configuration.getLogTag() == null
                ? getClass().getSimpleName()
                : configuration.getLogTag() + LOG_TAG_CONNECTOR + getClass().getSimpleName();
    }

    @Override
    public void publish(Scalpel scalpel) {
        scalpel.addFieldWirer(this);
    }

    protected boolean debug() {
        return debug;
    }

    protected void logV(Object o) {
        if (debug) Log.v(logTag, String.valueOf(o));
    }

    protected void logD(Object o) {
        if (debug) Log.d(logTag, String.valueOf(o));
    }

    protected void logE(Object o) {
        Log.e(logTag, String.valueOf(o));
    }
}

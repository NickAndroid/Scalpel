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
package com.nick.scalpel.core.opt;

import android.support.annotation.NonNull;

import com.nick.scalpel.core.utils.Preconditions;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SharedExecutor implements Executor {

    private static SharedExecutor sExecutor = null;

    private ExecutorService mService;
    private AtomicBoolean mDisposed = new AtomicBoolean(false);

    private SharedExecutor() {
        mService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public synchronized static SharedExecutor get() {
        if (sExecutor == null) {
            sExecutor = new SharedExecutor();
        }
        return sExecutor;
    }

    public void dispose() {
        Preconditions.checkState(!mDisposed.get());
        mDisposed.set(true);
        mService.shutdown();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        mService.execute(command);
    }
}
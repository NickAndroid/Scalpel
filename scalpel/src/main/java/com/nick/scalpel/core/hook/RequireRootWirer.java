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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.nick.scalpel.annotation.hook.RequireRoot;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsClassWirer;
import com.nick.scalpel.core.opt.SharedExecutor;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class RequireRootWirer extends AbsClassWirer {

    RootRequester mRequester;

    public RequireRootWirer(Configuration configuration, @NonNull RootRequester requester) {
        super(configuration);
        this.mRequester = requester;
    }

    @Override
    public void wire(Object o) {

        RequireRoot autoRequireRoot = o.getClass().getAnnotation(RequireRoot.class);
        RequireRoot.Mode mode = autoRequireRoot.mode();

        switch (mode) {
            case Sync:
                logV("Requesting root sync.");
                mRequester.requestRoot();
                break;
            case Async:
                RequireRoot.Callback callback;
                String callbackStr = autoRequireRoot.callback();
                Preconditions.checkState(!TextUtils.isEmpty(callbackStr), "Should specify a callback when using Async mode.");
                switch (callbackStr) {
                    case "this":
                        Preconditions.checkState(o instanceof RequireRoot.Callback, "Invalid callback reference.");
                        callback = (RequireRoot.Callback) o;
                        break;
                    default:
                        Field field = ReflectionUtils.findField(o, callbackStr);
                        Preconditions.checkNotNull(field, "No field named:" + field);
                        Object fieldObject = ReflectionUtils.getField(field, o);
                        Preconditions.checkNotNull(fieldObject, "Null field:" + field);
                        Preconditions.checkState(fieldObject instanceof RequireRoot.Callback, "Invalid callback reference.");
                        callback = (RequireRoot.Callback) fieldObject;
                        break;
                }
                logV("Requesting root async, callback:" + callback);
                SharedExecutor.get().execute(new AsyncRootRequester(callback));
        }
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RequireRoot.class;
    }

    class AsyncRootRequester implements Runnable {

        RequireRoot.Callback callback;

        public AsyncRootRequester(@NonNull RequireRoot.Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.onRootResult(mRequester.requestRoot(), mRequester.getShell());
        }
    }

}

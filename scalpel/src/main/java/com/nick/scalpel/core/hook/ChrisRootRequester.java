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

import com.chrisplus.rootmanager.RootManager;
import com.chrisplus.rootmanager.container.Result;

class ChrisRootRequester implements RootRequester {

    @Override
    public boolean requestRoot() {
        return RootManager.getInstance().obtainPermission();
    }

    @Override
    public Shell getShell() {
        return new AsyncShell() {
            @Override
            public boolean exec(String command, FeedbackReceiver receiver) {
                Result result = RootManager.getInstance().runCommand(command);
                if (receiver != null) receiver.onFeedback(result.getMessage());
                return result.getResult();
            }
        };
    }
}

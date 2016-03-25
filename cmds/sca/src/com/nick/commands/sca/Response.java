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
package com.nick.commands.sca;

public interface Response {
    int START_OK = 0x1;
    int START_FAILURE_ALREADY_STARTED = 0x2;
    int START_FAILURE_SYSTEM_ERR = 0x3;

    int STOP_OK = 0x4;
    int STOP_FAILURE_NOT_STARTED = 0x5;
    int STOP_FAILURE_SYSTEM_ERR = 0x6;

    int BAD_REQUEST = 0;
}

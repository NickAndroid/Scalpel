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

import java.util.StringTokenizer;

public class Feedback {

    static final String TOKEN_DIVIDER = "|";

    int code;
    String message;

    public Feedback(int code, String message) {
        if (message == null) throw new NullPointerException("Message cannot be null.");
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return code + TOKEN_DIVIDER + message;
    }

    public static Feedback fromString(String s) {
        if (s == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(s, TOKEN_DIVIDER);
        int code = Integer.parseInt(tokenizer.nextToken());
        String message = tokenizer.nextToken();
        return new Feedback(code, message);
    }
}

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

package com.nick.scalpel.config;

public class Configuration {

    boolean debug;
    boolean autoFindIfNull;
    String logTag;

    public static final Configuration DEFAULT = new Configuration();

    private Configuration(boolean debug, boolean autoFindIfNull, String logTag) {
        this.debug = debug;
        this.autoFindIfNull = autoFindIfNull;
        this.logTag = logTag;
    }

    private Configuration() {
        // Noop.
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isAutoFindIfNull() {
        return autoFindIfNull;
    }

    public String getLogTag() {
        return logTag;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Builder() {
            // Noop.
        }

        boolean debug;
        boolean autoFindIfNull;
        String logTag = "Scalpel";

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder autoFindIfNull(boolean autoFindIfNull) {
            this.autoFindIfNull = autoFindIfNull;
            return this;
        }

        public Builder logTag(String tag) {
            this.logTag = tag;
            return this;
        }

        public Configuration build() {
            return new Configuration(debug, autoFindIfNull, logTag);
        }
    }
}

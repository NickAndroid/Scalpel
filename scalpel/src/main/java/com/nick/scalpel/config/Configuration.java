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

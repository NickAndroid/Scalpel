package com.nick.commands.sca;

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

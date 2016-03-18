package com.nick.scalpel.intarnal;

public enum Type {

    View(android.view.View.class),
    Color(int.class),
    String(String.class),
    Bool(boolean.class),
    Integer(int.class),
    StringArray(String[].class),
    IntArray(int[].class);

    Class targetClass;

    Type(Class targetClass) {
        this.targetClass = targetClass;
    }
}

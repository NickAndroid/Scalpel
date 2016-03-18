/**
 * Copyright(c) 2016 Yangzhou New Telecom Science & Technology Co., Ltd. All rights served.
 */
package com.nick.scalpel.intarnal;

public class ClassInjection {
    public static void wire(Object o, ClassWirer... wirers) {
        Class cls = o.getClass();
        for (ClassWirer wirer : wirers) {
            if (cls.isAnnotationPresent(wirer.annotationClass())) {
                wirer.wire(o);
            }
        }
    }
}

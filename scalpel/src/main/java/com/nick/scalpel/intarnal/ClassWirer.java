/**
 * Copyright(c) 2016 Yangzhou New Telecom Science & Technology Co., Ltd. All rights served.
 */
package com.nick.scalpel.intarnal;

import java.lang.annotation.Annotation;

public interface ClassWirer {
    void wire(Object o);

    Class<? extends Annotation> annotationClass();
}
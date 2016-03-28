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

package com.nick.scalpel.core.opt;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import com.nick.scalpel.R;

import org.xmlpull.v1.XmlPullParser;

public abstract class AbsBeanXmlParser {

    static final String LOG_TAG = "Scalpel.XmlParser";

    private Context mContext;

    public AbsBeanXmlParser(@NonNull Context context) {
        this.mContext = context;
    }

    public void parse(int xmlRes) {
        String nameSpace = mContext.getResources().getString(R.string.bean_name_space);
        try {
            XmlResourceParser parser = mContext.getResources().getXml(xmlRes);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG &&
                        parser.getName().equals(nameSpace)) {
                    final TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.bean);
                    int id = a.getResourceId(R.styleable.bean_identify, 0);
                    String name = a.getString(R.styleable.bean_nickname);
                    String clz = a.getString(R.styleable.bean_clz);
                    if (clz != null) {
                        onCreateBeanItem(new BeanItem(id, name, clz));
                    }
                    a.recycle();
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Received exception parsing bean xml:" + Log.getStackTraceString(e));
        }
    }

    protected void onCreateBeanItem(BeanItem item) {
        // Noop.
    }
}

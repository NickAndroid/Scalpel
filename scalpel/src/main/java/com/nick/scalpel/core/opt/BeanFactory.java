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

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.nick.scalpel.annotation.opt.RetrieveBean;
import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.AbsFieldWirer;
import com.nick.scalpel.core.utils.Preconditions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.nick.scalpel.core.utils.ReflectionUtils.getField;
import static com.nick.scalpel.core.utils.ReflectionUtils.makeAccessible;
import static com.nick.scalpel.core.utils.ReflectionUtils.setField;

public class BeanFactory extends AbsFieldWirer implements Recyclable {

    private Context mContext;

    private final Map<BeanItem, Object> mBeanMap;

    private static BeanFactory sFactory;

    public static synchronized BeanFactory getInstance() {
        return Preconditions.checkNotNull(sFactory, "BeanFactory NOT init!");
    }

    public static void init(Context context, Configuration configuration, int... xmlRes) {
        sFactory = new BeanFactory(context, configuration, xmlRes);
    }

    private BeanFactory(Context context, Configuration configuration, int... xmlRes) {
        super(configuration);
        this.mContext = context;
        this.mBeanMap = new HashMap<>();
        for (int res : xmlRes) {
            if (res > 0) readPrebuilt(context, res);
        }
    }

    private void readPrebuilt(Context context, int res) {
        new AbsBeanXmlParser(context) {
            @Override
            protected void onCreateBeanItem(BeanItem item) {
                super.onCreateBeanItem(item);
                synchronized (mBeanMap) {
                    if (!mBeanMap.containsKey(item)) {
                        Object bean = createBean(item.clz);
                        logV("Created prebuilt bean:" + bean + ", for:" + item);
                        if (bean != null) {
                            mBeanMap.put(item, bean);
                            // Find supers
                            // cacheForSuper(bean.getClass(), bean, item);
                            cacheForInterface(bean.getClass(), bean, item);
                        }
                    }
                }
            }

            void cacheForSuper(Class clz, Object bean, BeanItem createdItem) {
                Class superClz = clz.getSuperclass();
                if (superClz == null) return;
                if (superClz == Object.class) return;
                BeanItem item = new BeanItem(createdItem.id, createdItem.name, superClz.getName());
                logV("Caching for super clz:" + item);
                if (!mBeanMap.containsKey(item)) mBeanMap.put(item, bean);
                else {
                    throw new IllegalStateException("Found multiple class beans for:" + clz);
                }
                cacheForSuper(superClz, bean, item);
            }

            void cacheForInterface(Class clz, Object bean, BeanItem createdItem) {
                Class[] interfaces = clz.getInterfaces();
                if (interfaces == null) return;
                for (Class iface : interfaces) {
                    BeanItem item = new BeanItem(createdItem.id, createdItem.name, iface.getName());
                    logV("Caching for iface:" + item);
                    if (!mBeanMap.containsKey(item)) mBeanMap.put(item, bean);
                    else {
                        logE("Found multiple interface beans for:" + mBeanMap.get(item));
                    }
                }
            }
        }.parse(res);
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RetrieveBean.class;
    }

    @Override
    public void wire(Activity activity, Field field) {
        autoWire(activity, field);
    }

    @Override
    public void wire(Fragment fragment, Field field) {
        autoWire(fragment, field);
    }

    @Override
    public void wire(Service service, Field field) {
        autoWire(service, field);
    }

    @Override
    public void wire(Context context, Object object, Field field) {
        autoWire(object, field);
    }

    @Override
    public void wire(View root, Object object, Field field) {
        autoWire(object, field);
    }

    private void autoWire(Object object, Field field) {
        makeAccessible(field);
        Object fieldObject = getField(field, object);

        if (fieldObject != null) return;

        RetrieveBean retrieveBean = field.getAnnotation(RetrieveBean.class);
        boolean singleton = retrieveBean.singleton();
        int id = retrieveBean.id();

        if (id > 0) {
            Object bean = getBeanById(id);
            setField(field, object, Preconditions.checkNotNull(bean, "No such bean with id:" + id));
            return;
        }

        Class clz = field.getType();

        if (!singleton) {
            setField(field, object, createBeanAndCache(clz));
        } else {
            Object existingBean = findBeanByClass(clz, id);
            if (existingBean != null) {
                logD("Using existing bean:" + existingBean + ", for:" + field);
                setField(field, object, existingBean);
            } else {
                setField(field, object, createBeanAndCache(clz));
            }
        }
    }

    private Object findBeanByClass(Class clz, int id) {
        synchronized (mBeanMap) {
            if (debug()) {
                for (BeanItem item : mBeanMap.keySet()) {
                    logV("Bean item: " + item);
                }
            }
            BeanItem item = new BeanItem(id, null, clz.getName());
            logV("Try finding an existing bean by:" + item);
            return mBeanMap.get(item);
        }
    }

    private Object createBeanAndCache(Class clz) {
        Object created = createBean(clz.getName());
        if (created != null) {
            logV("Cache bean " + created + ", for:" + clz.getName());
            mBeanMap.put(new BeanItem(clz.getName()), created);
        }
        return created;
    }

    @SuppressWarnings({"unchecked", "TryWithIdenticalCatches"})
    Object createBean(String clzName) {
        Class clz;
        try {
            clz = Class.forName(clzName);
        } catch (ClassNotFoundException e) {
            logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            return null;
        }
        // Find empty constructor.
        Constructor constructor = null;
        try {
            constructor = clz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logE("No empty constructor for:" + clzName);
        }
        if (constructor != null) {
            makeAccessible(constructor);
            try {
                return constructor.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            } catch (InvocationTargetException e) {
                logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            }
        }
        logD("Can not find empty Constructor for class:" + clzName);
        // Find Context constructor.
        try {
            constructor = clz.getDeclaredConstructor(Context.class);
        } catch (NoSuchMethodException e) {
            logE("No context-ed constructor for:" + clzName);
        }
        if (constructor != null) {
            makeAccessible(constructor);
            try {
                return constructor.newInstance(mContext);
            } catch (InstantiationException e) {
                logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            } catch (IllegalAccessException e) {
                logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            } catch (InvocationTargetException e) {
                logE("Error when create bean for:" + clzName + "\n" + Log.getStackTraceString(e));
            }
        }
        logE("Failed to create bean for:" + clzName);
        return null;
    }

    public Object getBeanById(int id) {
        synchronized (mBeanMap) {
            for (BeanItem item : mBeanMap.keySet()) {
                if (item.id == id) return mBeanMap.get(item);
            }
        }
        return null;
    }

    public Object getBeanByName(String name) {
        Preconditions.checkState(!TextUtils.isEmpty(name), "Invalid name:" + name);
        synchronized (mBeanMap) {
            for (BeanItem item : mBeanMap.keySet()) {
                if (name.equalsIgnoreCase(item.name)) return mBeanMap.get(item);
            }
        }
        return null;
    }

    @Override
    public void recycle() {
        mBeanMap.clear();
    }
}

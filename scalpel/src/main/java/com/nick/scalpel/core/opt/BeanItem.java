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

import android.support.annotation.NonNull;

import com.nick.scalpel.annotation.opt.RetrieveBean;

public class BeanItem {

    int id = RetrieveBean.DEFAULT_ID;
    String name;
    @NonNull
    String clz;

    public BeanItem(int id, String name, @NonNull String clz) {
        this.id = id;
        this.name = name;
        this.clz = clz;
    }

    public BeanItem(@NonNull String clz) {
        this.clz = clz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanItem item = (BeanItem) o;

        if (id != item.id) return false;
        return clz.equals(item.clz);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + clz.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BeanItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", clz='" + clz + '\'' +
                '}';
    }
}

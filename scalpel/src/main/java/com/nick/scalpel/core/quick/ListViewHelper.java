package com.nick.scalpel.core.quick;

import android.text.TextUtils;
import android.widget.ListView;

import com.nick.scalpel.annotation.quick.DataProvider;
import com.nick.scalpel.core.utils.Preconditions;

import java.lang.reflect.Field;

/**
 * Created by guohao4 on 2016/6/20.
 */
public class ListViewHelper implements ViewHelper<ListView> {
    @Override
    public void doExtendedHelp(ListView view, Field field) {

        DataProvider provider = field.getAnnotation(DataProvider.class);
        if (provider == null) return;
        String providerName = provider.name();
        Preconditions.checkState(!TextUtils.isEmpty(providerName));
        ListViewDataProvider listViewDataProvider;

    }
}

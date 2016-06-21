package com.nick.scalpel.core.quick;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ListView;

import com.nick.scalpel.annotation.quick.DataProvider;
import com.nick.scalpel.annotation.quick.ViewProvider;
import com.nick.scalpel.core.binding.ThisThatNull;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by guohao4 on 2016/6/20.
 */
class ListViewHelper implements ViewHelper<ListView> {

    Context mContext;

    ListViewHelper(Context context) {
        this.mContext = context;
    }

    @Override
    public void doExtendedHelp(ListView view, Field field, Object targetObj) {

        DataProvider provider = field.getAnnotation(DataProvider.class);
        if (provider == null) return;
        String providerName = provider.name();
        Preconditions.checkState(!TextUtils.isEmpty(providerName));

        Object listViewDataProviderObj;

        switch (providerName) {
            case ThisThatNull.THIS:
                listViewDataProviderObj = targetObj;
                break;
            default:
                Field providerField = ReflectionUtils.findField(targetObj, providerName);
                Preconditions.checkNotNull(providerField);
                ReflectionUtils.makeAccessible(providerField);
                listViewDataProviderObj = ReflectionUtils.getField(providerField, targetObj);
                Preconditions.checkNotNull(listViewDataProviderObj);
        }

        Preconditions.checkState(listViewDataProviderObj instanceof ListViewDataProvider);
        ListViewDataProvider listViewDataProvider = (ListViewDataProvider) listViewDataProviderObj;

        ListViewViewProvider listViewViewProvider = null;

        ViewProvider viewProvider = field.getAnnotation(ViewProvider.class);
        if (viewProvider != null) {
            final int id = viewProvider.id();
            listViewViewProvider = new ListViewViewProvider() {

                @Override
                public int getItemViewId() {
                    return id;
                }
            };
        }

        QuickAdapter quickAdapter = new QuickAdapter(listViewDataProvider,
                listViewViewProvider, view.getContext());// Using view.context to keep the activity theme.
        view.setAdapter(quickAdapter);
    }
}

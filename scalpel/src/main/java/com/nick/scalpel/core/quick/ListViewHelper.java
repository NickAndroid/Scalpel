package com.nick.scalpel.core.quick;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ListView;

import com.nick.scalpel.Scalpel;
import com.nick.scalpel.Scope;
import com.nick.scalpel.annotation.binding.MainThreadHandler;
import com.nick.scalpel.annotation.quick.DataProvider;
import com.nick.scalpel.annotation.quick.ViewProvider;
import com.nick.scalpel.core.binding.ThisThatNull;
import com.nick.scalpel.core.opt.SharedExecutor;
import com.nick.scalpel.core.utils.Preconditions;
import com.nick.scalpel.core.utils.ReflectionUtils;

import java.lang.reflect.Field;

class ListViewHelper implements ViewHelper<ListView> {

    @MainThreadHandler
    Handler mHandler;

    ListViewHelper(Context context) {
        Scalpel.getInstance().wire(context, this, Scope.Field);
    }

    @Override
    public void doExtendedHelp(final ListView view, Field field, Object targetObj) {

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
        final ListViewDataProvider listViewDataProvider = (ListViewDataProvider) listViewDataProviderObj;

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

        final ListViewViewProvider finalListViewViewProvider = listViewViewProvider;
        SharedExecutor.get().execute(new Runnable() {
            @Override
            public void run() {
                listViewDataProvider.loadInBackground();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        QuickAdapter quickAdapter = new QuickAdapter(listViewDataProvider,
                                finalListViewViewProvider, view.getContext());// Using view.context to keep the activity theme.
                        view.setAdapter(quickAdapter);
                    }
                });
            }
        });
    }
}

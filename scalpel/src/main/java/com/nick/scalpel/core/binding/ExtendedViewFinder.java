package com.nick.scalpel.core.binding;

import android.view.View;

import com.nick.scalpel.config.Configuration;
import com.nick.scalpel.core.opt.BeanFactory;
import com.nick.scalpel.core.quick.ViewHelper;

import java.lang.reflect.Field;

/**
 * Created by guohao4 on 2016/6/20.
 */
public class ExtendedViewFinder extends ViewFinder {

    public ExtendedViewFinder(Configuration configuration) {
        super(configuration);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onViewFound(View view, Field field, Object targetObj) {
        super.onViewFound(view, field, targetObj);
        String clzName = view.getClass().getSimpleName();
        ViewHelper helper = (ViewHelper) BeanFactory.getInstance().getBeanByName(clzName + "_helper");

        if (helper != null) {
            helper.doExtendedHelp(view, field, targetObj);
            if (debug()) logD("doExtendedHelp end, from:" + helper);
        }
    }
}

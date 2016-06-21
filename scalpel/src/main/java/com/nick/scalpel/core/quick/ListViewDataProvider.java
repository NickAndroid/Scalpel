package com.nick.scalpel.core.quick;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by guohao4 on 2016/6/17.
 */
public interface ListViewDataProvider {

    interface ImageCallback<T> {
        T onRequestImage(int position);
    }

    interface DrawableCallback extends ImageCallback<Drawable> {

    }

    interface BitmapCallback extends ImageCallback<Bitmap> {

    }

    interface TextCallback<T> {
        @NonNull
        T onRequestText(int position);
    }

    interface DataCallback<T extends List> {
        @NonNull
        T getData();
    }


    @Nullable
    ImageCallback getImageCallback();

    @Nullable
    DataCallback getDataCallback();

    @Nullable
    TextCallback getTextCallback();

    void loadInBackground();
}

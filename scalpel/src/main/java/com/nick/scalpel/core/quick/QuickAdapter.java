package com.nick.scalpel.core.quick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nick.scalpel.core.utils.Preconditions;

/**
 * Created by guohao4 on 2016/6/17.
 */
public class QuickAdapter extends BaseAdapter {

    private DataProvider mDataProvider;
    private ViewProvider mViewProvider;
    private Context mContext;

    public QuickAdapter(DataProvider dataProvider, ViewProvider viewProvider, Context context) {
        this.mDataProvider = Preconditions.checkNotNull(dataProvider);
        this.mViewProvider = viewProvider;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDataProvider == null ? 0 : (mDataProvider.getDataCallback().getData()).size();
    }

    @Override
    public Object getItem(int position) {
        return mDataProvider == null ? null : (mDataProvider.getDataCallback().getData()).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            View createdView = LayoutInflater.from(mContext).inflate(mViewProvider.getItemViewId(), parent, false);
            holder = new ViewHolder(createdView, position);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.setPosition(position);
            Log.d("Nick", "using existed holder.");
        }

        return bindView(holder);
    }

    View bindView(ViewHolder holder) {
        int position = holder.position;

        DataProvider.TextCallback textCallback = mDataProvider.getTextCallback();
        String text = null;
        if (textCallback == null) {
            DataProvider.DataCallback dataCallback = mDataProvider.getDataCallback();
            if (dataCallback != null)
                text = String.valueOf(mDataProvider.getDataCallback().getData().get(position));
        } else {
            text = String.valueOf(textCallback.onRequestText(position));
        }

        holder.setText(text);

        DataProvider.ImageCallback imageCallback = mDataProvider.getImageCallback();
        if (imageCallback == null) {
            holder.hideImage();
        } else {
            Object image = imageCallback.onRequestImage(position);
            if (image instanceof Bitmap) {
                Bitmap bm = (Bitmap) image;
                holder.setImage(bm);
            }
            if (image instanceof Drawable) {
                Drawable dr = (Drawable) image;
                holder.setImage(dr);
            }
        }

        return holder.convertView;
    }

    final class ViewHolder {

        ImageView imageView;
        TextView textView;
        int position;

        View convertView;

        public ViewHolder(View convertView, int position) {
            Preconditions.checkNotNull(convertView);
            this.imageView = (ImageView) convertView.findViewById(android.R.id.icon);
            this.textView = (TextView) convertView.findViewById(android.R.id.title);
            this.position = position;
            this.convertView = convertView;
            this.convertView.setTag(this);
        }

        public void setPosition(int position) {
            this.position = position;
        }

        void setImage(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        void setImage(Drawable drawable) {
            imageView.setImageDrawable(drawable);
        }

        void hideImage() {
            imageView.setVisibility(View.GONE);
        }

        void setText(String s) {
            textView.setText(s);
        }
    }
}

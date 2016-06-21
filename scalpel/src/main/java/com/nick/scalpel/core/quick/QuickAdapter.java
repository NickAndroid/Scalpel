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

import com.nick.scalpel.R;
import com.nick.scalpel.core.utils.Preconditions;

public class QuickAdapter extends BaseAdapter {

    private ListViewDataProvider mListViewDataProvider;
    private ListViewViewProvider mListViewViewProvider;
    private Context mContext;

    public QuickAdapter(ListViewDataProvider ListViewDataProvider, ListViewViewProvider listViewViewProvider, Context context) {
        this.mListViewDataProvider = Preconditions.checkNotNull(ListViewDataProvider);
        this.mListViewViewProvider = listViewViewProvider;
        this.mContext = context;
    }

    public int getItemViewId() {
        return mListViewViewProvider == null ? R.layout.simple_list_item : mListViewViewProvider.getItemViewId();
    }

    @Override
    public int getCount() {
        return mListViewDataProvider == null ? 0 : (mListViewDataProvider.getDataCallback().getData()).size();
    }

    @Override
    public Object getItem(int position) {
        return mListViewDataProvider == null ? null : (mListViewDataProvider.getDataCallback().getData()).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            View createdView = LayoutInflater.from(mContext).inflate(getItemViewId(), parent, false);
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

        ListViewDataProvider.TextCallback textCallback = mListViewDataProvider.getTextCallback();
        String text = null;
        if (textCallback == null) {
            ListViewDataProvider.DataCallback dataCallback = mListViewDataProvider.getDataCallback();
            if (dataCallback != null)
                text = String.valueOf(mListViewDataProvider.getDataCallback().getData().get(position));
        } else {
            text = String.valueOf(textCallback.onRequestText(position));
        }

        holder.setText(text);

        ListViewDataProvider.ImageCallback imageCallback = mListViewDataProvider.getImageCallback();
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
            if (imageView == null) return;
            imageView.setImageBitmap(bitmap);
        }

        void setImage(Drawable drawable) {
            if (imageView == null) return;
            imageView.setImageDrawable(drawable);
        }

        void hideImage() {
            if (imageView == null) return;
            imageView.setVisibility(View.GONE);
        }

        void setText(String s) {
            textView.setText(s);
        }
    }
}

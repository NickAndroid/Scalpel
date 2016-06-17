package com.nick.scalpeldemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.core.quick.DataProvider;
import com.nick.scalpel.core.quick.QuickAdapter;
import com.nick.scalpel.core.quick.ViewProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohao4 on 2016/6/17.
 */
public class QuickList extends ScalpelAutoActivity {

    @FindView(id=R.id.listView)
    @com.nick.scalpel.annotation.quick.DataProvider(name = "mDataProvider")
    @com.nick.scalpel.annotation.quick.ViewProvider(id=R.layout.item_view)
    ListView listView;

    DataProvider mDataProvider = new DataProvider() {

        @Nullable
        @Override
        public ImageCallback getImageCallback() {
            return null;
        }

        @Nullable
        @Override
        public DataCallback getDataCallback() {
            return new DataCallback() {
                @NonNull
                @Override
                public List getData() {
                    return createData();
                }
            };
        }

        @Nullable
        @Override
        public TextCallback getTextCallback() {
            return null;
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quick_list);

        final List<String> createData = createData();

        DataProvider dataProvider = new DataProvider() {

            @Nullable
            @Override
            public ImageCallback getImageCallback() {
                return null;
            }

            @Nullable
            @Override
            public DataCallback getDataCallback() {
                return new DataCallback() {
                    @NonNull
                    @Override
                    public List getData() {
                        return createData;
                    }
                };
            }

            @Nullable
            @Override
            public TextCallback getTextCallback() {
                return null;
            }
        };

        ViewProvider viewProvider = new ViewProvider(R.layout.item_view);

        QuickAdapter adapter = new QuickAdapter(dataProvider, viewProvider, this);

        listView.setAdapter(adapter);
    }

    List<String> createData() {
        List<String> out = new ArrayList<String>();
        for (int i =0; i < 1000; i++) {
            out.add("ABCDE.No." + i);
        }
        return out;
    }
}

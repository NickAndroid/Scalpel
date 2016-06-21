package com.nick.scalpeldemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.annotation.quick.DataProvider;
import com.nick.scalpel.core.quick.ListViewDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohao4 on 2016/6/17.
 */
public class QuickList extends ScalpelAutoActivity {

    private List<String> mData;

    @FindView(id=R.id.listView)
    @DataProvider(name = "mListViewDataProvider")
    //@ViewProvider(id=R.layout.item_view)
    ListView listView;

    ListViewDataProvider mListViewDataProvider = new ListViewDataProvider() {

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
                    return mData;
                }
            };
        }

        @Nullable
        @Override
        public TextCallback getTextCallback() {
            return null;
        }

        @Override
        public void loadInBackground() {
            mData = createData();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_list);
    }

    List<String> createData() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> out = new ArrayList<String>();
        for (int i =0; i < 1000; i++) {
            out.add("ABCDE.No." + i);
        }
        return out;
    }
}

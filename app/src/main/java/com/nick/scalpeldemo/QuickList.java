package com.nick.scalpeldemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.nick.scalpel.ScalpelAutoActivity;
import com.nick.scalpel.annotation.binding.FindView;
import com.nick.scalpel.annotation.quick.DataProvider;
import com.nick.scalpel.annotation.quick.ViewProvider;
import com.nick.scalpel.core.quick.ListViewDataProvider;
import com.nick.scalpel.core.quick.QuickAdapter;
import com.nick.scalpel.core.quick.ListViewViewProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guohao4 on 2016/6/17.
 */
public class QuickList extends ScalpelAutoActivity {

    @FindView(id=R.id.listView)
    @DataProvider(name = "mListViewDataProvider")
    @ViewProvider(id=R.layout.item_view)
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

        ListViewDataProvider ListViewDataProvider = new ListViewDataProvider() {

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

        ListViewViewProvider listViewViewProvider = new ListViewViewProvider(R.layout.item_view);

        QuickAdapter adapter = new QuickAdapter(ListViewDataProvider, listViewViewProvider, this);

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

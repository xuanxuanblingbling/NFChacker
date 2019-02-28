package com.xuanxuan.NFChacker.OtherView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.xuanxuan.NFChacker.R;
import com.xuanxuan.NFChacker.ToolsUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditActivity extends Activity {
    private ListView mListView;
    private Button mButton;
    private Button bt;
    private ListViewAdapter mAdapter;
    private List<ItemBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mListView = (ListView) findViewById(R.id.list_view);
        mButton = (Button) findViewById(R.id.button);
        mData = new ArrayList<ItemBean>();


        Map<String,String> myMap= ToolsUnit.currentMap;
        for(Map.Entry<String,String> entry:myMap.entrySet()){
            mData.add(new ItemBean(entry.getKey(),entry.getValue()));
        }

        mAdapter = new ListViewAdapter(this, mData);
        ToolsUnit.tableAdapter = mAdapter;
        mListView.setAdapter(mAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.add(new ItemBean());
                mAdapter.notifyDataSetChanged();
            }
        });

    }
}

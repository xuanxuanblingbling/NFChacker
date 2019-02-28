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

public class RuleActivity extends Activity {

    private ListView mListView;
    private Button mButton;
    private ListViewAdapter mAdapter;
    private List<ItemBean> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        mListView = (ListView) findViewById(R.id.list_view_rule);
        mButton = (Button) findViewById(R.id.button_rule);
        mData = new ArrayList<ItemBean>();


        Map<String,String> myMap= ToolsUnit.currentRule;
        for(Map.Entry<String,String> entry:myMap.entrySet()){
            mData.add(new ItemBean(entry.getKey(),entry.getValue()));
        }

        mAdapter = new ListViewAdapter(this, mData);

        ToolsUnit.ruleAdapter=mAdapter;

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

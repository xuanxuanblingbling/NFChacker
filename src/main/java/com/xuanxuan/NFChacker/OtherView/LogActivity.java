package com.xuanxuan.NFChacker.OtherView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanxuan.NFChacker.R;
import com.xuanxuan.NFChacker.ToolsUnit;

import java.util.ArrayList;

public class LogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ArrayList<String> myFile = ToolsUnit.listDir("log");

        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,myFile);
        final TextView tvv = (TextView) findViewById(R.id.tvv);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setAdapter(listadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> myAdapter = (ArrayAdapter<String>)adapterView.getAdapter();
                tvv.setText(ToolsUnit.getFile("log/"+myAdapter.getItem(i)));
                Toast.makeText(LogActivity.this,myAdapter.getItem(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}

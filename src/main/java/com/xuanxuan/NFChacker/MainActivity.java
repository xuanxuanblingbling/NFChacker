package com.xuanxuan.NFChacker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xuanxuan.NFChacker.OtherView.EditActivity;
import com.xuanxuan.NFChacker.OtherView.LogActivity;
import com.xuanxuan.NFChacker.OtherView.RuleActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private CardEmulation mCardEmulation;
    private TextView tv;
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    private Button bt6;
    private EditText et1;
    private EditText et2;
    private Spinner sp;
    private ArrayList<String> myFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }else {
                initView();
            }
        }else {
            initView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initView();
                }else{
                    finish();
                }
                break;
        }
    }

    public void initView(){
        if(!ToolsUnit.fileExist("NFChacker")){
            initDir(this);
        }

        tv = (TextView) findViewById(R.id.textView1);
        ToolsUnit.tv=tv;

        bt1 = (Button)findViewById(R.id.button);
        bt2 = (Button)findViewById(R.id.button2);
        bt3 = (Button)findViewById(R.id.button3);
        bt4 = (Button)findViewById(R.id.button4);
        bt5 = (Button)findViewById(R.id.button5);
        bt6 = (Button)findViewById(R.id.button6);

        et1 = (EditText)findViewById(R.id.editText1);
        et2 = (EditText)findViewById(R.id.etsave);

        sp = (Spinner)findViewById(R.id.spinner);
        myFile = ToolsUnit.listDir("data");
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,myFile);
        sp.setAdapter(listadapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> myAdapter = (ArrayAdapter<String>)adapterView.getAdapter();
                ToolsUnit.changeCard(myAdapter.getItem(i));
                Toast.makeText(MainActivity.this,ToolsUnit.cardinfo,Toast.LENGTH_SHORT).show();
                tv.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolsUnit.RuleOrTable=true;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RuleActivity.class);
                startActivity(intent);
            }
        });



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToolsUnit.RuleOrTable=false;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });




        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });


        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String date = df.format(new Date());
                String payload = "["+ToolsUnit.getStingFromMap(ToolsUnit.currentMap)+","+ToolsUnit.getStingFromMap(ToolsUnit.currentRule)+"]";
                if(et2.getText().toString().equals("")){
                    ToolsUnit.saveFile(payload,"data", date+".txt");
                    Toast.makeText(MainActivity.this,date+".txt已保存",Toast.LENGTH_SHORT).show();
                }else {
                    ToolsUnit.saveFile(payload,"data", et2.getText().toString()+".txt");
                    Toast.makeText(MainActivity.this,et2.getText().toString()+".txt已保存",Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"当前记录为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    String date = df.format(new Date());
                    if(et1.getText().toString().equals("")){
                        ToolsUnit.saveFile(tv.getText().toString(),"log", date+".txt");
                        Toast.makeText(MainActivity.this,date+".txt已保存",Toast.LENGTH_SHORT).show();
                    }else {
                        ToolsUnit.saveFile(tv.getText().toString(),"log", et1.getText().toString()+".txt");
                        Toast.makeText(MainActivity.this,et1.getText().toString()+".txt已保存",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
                ToolsUnit.currentValue.clear();
            }
        });

        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        Log.d("MainActivityHost", "onCreate: adapter loaded");



        try{

            //设置为默认应用

            mCardEmulation = CardEmulation.getInstance(adapter);
            Log.d("MainActivityHost", "onCreate: mCardEmulation loaded");

            ComponentName myComponent = new ComponentName("com.xuanxuan.NFChacker",
                    "com.xuanxuan.NFChacker.PaymentServiceHost");

            Log.d("MainActivityHost", "onCreate: Mode for category payment: " + Integer.toString(mCardEmulation.getSelectionModeForCategory(CardEmulation.CATEGORY_PAYMENT)));
            Log.d("MainActivityHost", "onCreate: Mode for category other: " + Integer.toString(mCardEmulation.getSelectionModeForCategory(CardEmulation.CATEGORY_OTHER)));

            if (!mCardEmulation.isDefaultServiceForCategory(myComponent, CardEmulation.CATEGORY_PAYMENT)) {
                Intent intent = new Intent(CardEmulation.ACTION_CHANGE_DEFAULT);
                intent.putExtra(CardEmulation.EXTRA_CATEGORY, CardEmulation.CATEGORY_PAYMENT);
                intent.putExtra(CardEmulation.EXTRA_SERVICE_COMPONENT, myComponent);
                startActivityForResult(intent, 0);
            } else {
                Log.e("MainActivityHost", "on Create: Already default!");
            }
        }catch(Exception e){
            Toast.makeText(MainActivity.this,"您的手机不支持NFC",Toast.LENGTH_SHORT).show();
        }

    }

    public void initDir(Context context){
        ToolsUnit.mkDir("data");
        ToolsUnit.mkDir("log");
        ToolsUnit.saveFile(ToolsUnit.readAssetsTxt(context,"test"),"data", "测试用例.txt");
    }


}

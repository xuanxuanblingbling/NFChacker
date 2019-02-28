package com.xuanxuan.NFChacker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.xuanxuan.NFChacker.OtherView.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class ToolsUnit {

    public static TextView tv;
    public static Map<String,String> currentMap = new HashMap<String,String>();
    public static Map<String,String> currentValue= new HashMap<String,String>();
    public static Map<String,String> currentRule= new HashMap<String,String>();
    public static  String cardinfo;
    public static boolean RuleOrTable;
    public static ListViewAdapter ruleAdapter;
    public static ListViewAdapter tableAdapter;


    public static void changeCard(String card){
        cardinfo = "data/"+card;
        currentMap = getMapFromJson(getJsonFromString(getFile(cardinfo),0));
        currentRule = getMapFromJson(getJsonFromString(getFile(cardinfo),1));
    }

    public static String Start(Context context, byte[] apdu){
        return doRule(currentMap,currentRule,bytesToHex(apdu));
    }

    public static String getStingFromMap(Map<String,String> myMap){
        String payload="{\n";
        for(Map.Entry<String,String> entry:myMap.entrySet()){
            payload = payload+"\""+entry.getKey()+"\":"+"\""+entry.getValue()+"\",\n";
        }
        payload=payload.substring(0,payload.length()-2);
        payload=payload+"\n}";
        return payload;
    }

    public static  String doRule(Map<String,String> myMap ,Map<String,String> myRule,String apdu){
        Map<String,String> myValue = new HashMap<String, String>();
        for(Map.Entry<String,String> entry:myRule.entrySet()){
            String[] reader = entry.getKey().split("-");
            String[] card = entry.getValue().split("-");
            int length = reader[0].length();
            int subStart =  Integer.parseInt(reader[2]);
            int subLength =  Integer.parseInt(reader[3]);
            if(apdu.substring(0,length).equals(reader[0])){
                if(!reader[1].equals("0")){
                    myValue.put(reader[1],apdu.substring(subStart*2,subStart*2+subLength*2));
                    currentValue=myValue;
                }
                if (!card[1].equals("0")){
                    if (card[1].equals("*")){
                        return card[0]+getRand(Integer.parseInt(card[2]))+card[3];
                    }
                    else if(currentValue.containsKey(card[1])){
                        return card[0]+currentValue.get(card[1])+card[3];
                    }else {
                        return card[0]+card[2]+card[3];
                    }
                }else {
                    return card[0]+card[3];
                }
            }
        }
        for(Map.Entry<String,String> entry:myMap.entrySet()){
            if(entry.getKey().equals(apdu))return entry.getValue();
        }
        return "9000";
    }


    public static String getRand(int j){
        Random random = new Random();
        String result="";
        for (int i=0;i<j;i++)
        {
            result+=random.nextInt(10);
        }
        return result;
    }



    /**
     * 读取assets下的txt文件，返回utf-8 String
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName){
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName+".txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    public static JSONObject getJsonFromString(String s, int i) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(s);
            return jsonArray.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,String> getMapFromJson(JSONObject json){
        Map<String,String> myMap = new HashMap<String,String>();
        try {
            Iterator iterator = json.keys();
            while(iterator.hasNext()){
                String key = (String) iterator.next();
                myMap.put(key,json.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("parseJson: ","error");
        }
        return myMap;
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public static void mkDir(String dir){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/NFChacker/"+dir);
            if (!file.exists()) {
                Log.d("result", "create result:" + file.mkdirs());
            }
        }
    }

    public static boolean fileExist(String dir){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory()+"/"+dir);
            if (file.exists()) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


    public static void saveFile(String str, String dirName,String fileName){
        // 创建String对象保存文件名路径
        try {
            // 创建指定路径的文件
            File file = new File(Environment.getExternalStorageDirectory()+ "/NFChacker/"+dirName+"/", fileName);
            // 如果文件不存在
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            file.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(str.getBytes());
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> ChangeReader(Map<String,String> myMap,String oldReader,String newReader){
        String card = myMap.get(oldReader);
        myMap.put(newReader,card);
        myMap.remove(oldReader);
        return myMap;
    }

    public static Map<String,String> ChangeCard(Map<String,String> myMap,String Reader,String newCard){
        myMap.put(Reader,newCard);
        return myMap;
    }


    /**
     * 删除已存储的文件
     */
    public static void deletefile(String fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取文件里面的内容
     */
    public static String getFile(String fileName) {
        try {
            // 创建文件
            File file = new File(Environment.getExternalStorageDirectory()+"/NFChacker/",fileName);
            // 创建FileInputStream对象
            FileInputStream fis = new FileInputStream(file);
            // 创建字节数组 每次缓冲1M
            byte[] b = new byte[1024];
            int len = 0;// 一次读取1024字节大小，没有数据后返回-1.
            // 创建ByteArrayOutputStream对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 一次读取1024个字节，然后往字符输出流中写读取的字节数
            while ((len = fis.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            // 将读取的字节总数生成字节数组
            byte[] data = baos.toByteArray();
            // 关闭字节输出流
            baos.close();
            // 关闭文件输入流
            fis.close();
            // 返回字符串对象
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<String> listDir(String path){
        ArrayList<String> myFile = new ArrayList<String>();
        String realPath=Environment.getExternalStorageDirectory().toString()+"/NFChacker/"+path;
        File file=new File(realPath);
        File[] files=file.listFiles();
        for(int i =0;i<files.length;i++){
            myFile.add(files[i].getName());
         }
         return myFile;
    }

    public static byte[] hexStringToByteArray(String s) {
        s=s.toLowerCase();
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}

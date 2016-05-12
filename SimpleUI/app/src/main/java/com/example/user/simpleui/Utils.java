package com.example.user.simpleui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by user on 2016/4/28.
 */
public class Utils {

    public static void writeFile(Context context, String fileName, String content){ //static可以直接使用，不用先new物件
        try { //因為要花較多時間寫到system，所以需要try catch
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND); //APPEND 接著加, PRIVATE 重頭寫
            fos.write(content.getBytes()); //轉成Byte檔寫入
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName){

        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fis.read(buffer, 0, buffer.length);
            fis.close();
            return new String(buffer); //byte to string and return
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /** use example
        String[] data = Utils.readFile(this, "notes").split("\n");
        mTextView.setText(data[0]);

        Utils.writeFile(this, "notes", order.note + '\n');
        */

    public static Uri getPhotoURI(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);//get pictures folder position
        if(dir.exists() == false){ //check exists
            dir.mkdir();
        }

        File file = new File(dir, "simpleUI_photo.png");
        return Uri.fromFile(file);
    }

    public static byte[] uriToBytes(Context context, Uri uri){ //做存取需要用到context
        try { //檔案的存取就要使用try/catch
            //轉成byte寫進Array
            InputStream mInputStream = context.getContentResolver().openInputStream(uri); //
            ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = mInputStream.read(buffer)) != -1){
                mByteArrayOutputStream.write(buffer);
            }
            return mByteArrayOutputStream.toByteArray();//二維轉一維，並回傳
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] urlToBytes(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream mInputStream = connection.getInputStream();
            ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = mInputStream.read(buffer)) != -1){
                mByteArrayOutputStream.write(buffer,0,len);
            }
            return mByteArrayOutputStream.toByteArray();//二維轉一維，並回傳
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getGeoCodingUrl(String address){
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://maps.google.com.tw/maps/api/geocode/json?address=" + address;
        return url;
    }

    public static double[] addressToLatLng(String address){
        String url = Utils.getGeoCodingUrl(address);
        byte[] bytes = Utils.urlToBytes(url);
        if(bytes != null) {
            String result = new String(bytes);
            try {
                JSONObject object = new JSONObject(result);
                if(object.getString("status").equals("OK")){
                    JSONObject location = object.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");

                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");

                    return new double[]{lat,lng};
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Bitmap getStaticMap(double[] latlng){
        String center = latlng[0] + "," + latlng[1];
        String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + center + "&zoom=17&size=640x400";
        byte[] bytes = Utils.urlToBytes(staticMapUrl);
        if(bytes !=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        }
        return null;
    }
}

package com.example.user.simpleui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
}
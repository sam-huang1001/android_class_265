package com.example.user.simpleui;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.realm.RealmObject;

/**
 * Created by user on 2016/4/25.
 */
public class Order extends RealmObject{
    private String note;
    private String menuResults;
    private String storeInfo;

    byte[] photo = null;
    String photoURL = "";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMenuResults() {
        return menuResults;
    }

    public void setMenuResults(String menuResults) {
        this.menuResults = menuResults;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public  void saveToRemote(SaveCallback saveCallback){
        ParseObject parseObject = new ParseObject("Order");
        parseObject.put("note", note);
        parseObject.put("storeInfo", storeInfo);
        parseObject.put("menuResults", menuResults);
        if(this.photo != null){
            ParseFile file = new ParseFile("photo.png", this.photo);//容量較大使用parseFile存
            parseObject.put("photo", file);
        }


        parseObject.saveInBackground(saveCallback);
    }
}

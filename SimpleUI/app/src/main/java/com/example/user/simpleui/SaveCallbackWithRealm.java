package com.example.user.simpleui;

import com.parse.ParseException;
import com.parse.SaveCallback;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * 為了接realmObject並重新實作SaveCallback的done
 * Created by user on 2016/5/5.
 */
public class SaveCallbackWithRealm implements SaveCallback{
    RealmObject realmObject;
    SaveCallback saveCallback;

    //多了第一個參數，目的是要接realmObject
    public SaveCallbackWithRealm(RealmObject realmObject, SaveCallback callback){ //還是要設callback讓人家call
        this.realmObject = realmObject;
        this.saveCallback = callback;
    }

    @Override
    public void done(ParseException e) {
        if(e == null){
            Realm realm = Realm.getDefaultInstance();
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(realmObject);
            realm.commitTransaction();

            realm.close();
        }

        saveCallback.done(e); //外面實作的saveCallback
    }
}

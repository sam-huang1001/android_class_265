package com.example.user.simpleui;

import io.realm.RealmObject;

/**
 * Created by user on 2016/5/9.
 */
public class StoreInfo extends RealmObject {
    private String store;

    public void setStore(String store) {
        this.store = store;
    }
}

package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by user on 2016/5/5.
 */
public class SimpleUIApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("h5Fh0VOAx0PhwLiPVo7fgjYWb9ZTNwHI4JuMT0er")
                        .clientKey("GndPCC5XIgIF8DYZo5PQ6JHuYDsIm5nY9P14iZ5u")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
    }
}

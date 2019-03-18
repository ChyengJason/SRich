package com.jscheng.srich;

import android.app.Application;

import com.jscheng.srich.route.Router;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);
    }
}

package com.halove.business;

import android.app.Application;

/**
 * Created by xieshangwu on 2017/8/2
 */

public class App extends Application {

    private static App mApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    private static App getInstance() {
        return mApp;
    }
}

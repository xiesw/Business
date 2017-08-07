package com.halove.business;

import android.app.Application;

import com.halove.business.share.ShareManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by xieshangwu on 2017/8/2
 */

public class App extends Application {

    private static App mApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        Logger.addLogAdapter(new AndroidLogAdapter());
        initShareSDK();
    }

    public static App getInstance() {
        return mApp;
    }

    /**
     * shareSDK 初始化
     */
    public void initShareSDK() {
        ShareManager.init(this);
    }
}

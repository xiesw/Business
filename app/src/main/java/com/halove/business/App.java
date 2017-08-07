package com.halove.business;

import android.app.Application;

import com.halove.business.share.ShareManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

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
        initJPush();
        initYMENG();
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

    /**
     * 极光推送 初始化
     */
    public void initJPush() {
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    /**
     * 友盟统计 初始化
     */
    private void initYMENG() {
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
    }

}

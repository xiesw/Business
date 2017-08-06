
package com.halove.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.halove.business.App;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 配置文件工具类
 */
public class SharedPreferenceManager {

    private static SharedPreferences sp;
    private static SharedPreferenceManager spManager;
    private static Editor editor;
    /**
     * Preference文件名
     */
    private static final String SHARE_PREFREENCE_NAME = "imooc.pre";

    public static final String LAST_UPDATE_PRODUCT = "last_update_product";
    public static final String VIDEO_PLAY_SETTING = "video_play_setting";

    private SharedPreferenceManager() {
        sp = App.getInstance().getSharedPreferences(SHARE_PREFREENCE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SharedPreferenceManager getInstance() {
        if (spManager == null || sp == null || editor == null) {
            spManager = new SharedPreferenceManager();
        }
        return spManager;
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, int defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public boolean isKeyExist(String key) {
        return sp.contains(key);
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}

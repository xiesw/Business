package com.halove.core;

/**
 * Created by xieshangwu on 2017/8/4
 * 核心库的一些常量
 */

public class SDKConstants {

    //毫秒单位
    public static int MILLION_UNIT = 1000;

    public static final int VIDEO_SCREEN_PERCENT = 50;
    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;

    //自动播放条件
    public enum AutoPlaySetting {
        AUTO_PLAY_ONLY_WIFI,
        AUTO_PLAY_3G_4G_WIFI,
        AUTO_PLAY_NEVER;
    }
}

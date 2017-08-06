package com.halove.core.widget.video.sdk;

/**
 * Created by xieshangwu on 2017/8/6
 * @function 最终通知应用是否成功
 */

public interface VideoSdkInterface {
    void onVideoSuccess();

    void onVideoFailed();

    void onClickVideo(String url);
}

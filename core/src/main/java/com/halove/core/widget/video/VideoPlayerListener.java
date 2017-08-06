package com.halove.core.widget.video;

/**
 * Created by xieshangwu on 2017/8/4
 * @function 视频事件监听回调
 */

public interface VideoPlayerListener {

    void onBufferUpdate(int time);      // 视频播放到了第几秒

    void onClickFullScreenBtn();

    void onClickVideo();

    void onClickbackBtn();

    void onClickPlay();

    void onVideoLoadSuccess();

    void onVideoLoadFailed();

    void onVideoPlayComplete();
}

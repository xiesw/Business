package com.halove.core.widget.video;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.halove.core.SDKConstants;
import com.halove.core.activity.AdBrowserActivity;
import com.halove.core.entity.VideoVaule;
import com.halove.core.report.ReportManager;
import com.halove.core.utils.Utils;

/**
 * Created by xieshangwu on 2017/8/5
 * 业务逻辑层
 */

public class VideoSlot implements VideoPlayerListener {

    private Context mContext;

    /**
     * UI
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;

    private VideoVaule mVideoInfo;
    private AdSDKSlotListener mSlotListener;
    private boolean canPause = false;   // 是否可以自动暂停标志位
    private int lastArea = 0;       //防止将要滑入滑出时播放器的状态改变

    public VideoSlot(VideoVaule videoInfo, AdSDKSlotListener listener) {
        mVideoInfo = videoInfo;
        mSlotListener = listener;
        mParentView = listener.getAdParent();
        mContext = mParentView.getContext();
        initVideoView();
    }

    private void initVideoView() {
        mVideoView = new CustomVideoView(mContext, mParentView);
        if(mVideoView != null) {
            mVideoView.setDataSource(mVideoInfo.resource);
            mVideoView.setListener(this);
        }
    }

    /**
     * 实现滑入播放, 滑出
     */
    public void updataVideoInScrollView() {
        int currentArea = Utils.getVisiblePercent(mParentView);

        // 还未出现在屏幕
        if(currentArea <= 0) {
            return;
        }

        // 滑入滑出异常处理
        if(Math.abs(currentArea - lastArea) >= 100) {
            return;
        }

        // 滑动没有超过 50%
        if(currentArea <= SDKConstants.VIDEO_SCREEN_PERCENT) {
            if(canPause) {
                pauseVideo();
                canPause = false;
            }
            lastArea = 0;
            mVideoView.setIsComplete(false);
            mVideoView.setIsRealPause(false);
            return;
        }

        // 视频真正的进入暂停状态
        if(isRealPause() || isComplete()) {
            pauseVideo();
            canPause = false;
        }

        // TODO: 2017/8/5 满足视频播放条件
        if(true) {
            resumeVideo();
        } else {
            pauseVideo();
            mVideoView.setIsRealPause(true);
        }
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {
        //  将播放器用view树中移除
        mParentView.removeView(mVideoView);
        //  创建全屏播放Dialog
        VideoFullDialog dialog = new VideoFullDialog(mContext, mVideoView, mVideoInfo);
        dialog.setListener(new VideoFullDialog.FullToSmallListener() {
            @Override
            public void getCurrentPlayPosition(int position) {

            }

            @Override
            public void playComplete() {
                //全屏播放后事件回调
                bigPlayComplete();
            }
        });
        dialog.show();
    }

    private void bigPlayComplete() {
        if(mVideoView.getParent() == null) {
            mParentView.addView(mVideoView);
        }

        mVideoView.isShowFullBtn(true);
        mVideoView.setMute(true);
        mVideoView.setListener(this);
        mVideoView.seekAndResume(0);
        canPause = false;
    }

    private void backToSmallMode(int position) {
        if(mVideoView.getParent() == null) {
            mParentView.addView(mVideoView);
        }

        mVideoView.isShowFullBtn(true);
        mVideoView.setMute(true);
        mVideoView.setListener(this);
        mVideoView.seekAndResume(position);
    }



    @Override
    public void onClickVideo() {
        String desationUrl = mVideoInfo.clickUrl;
        //跳转到webView
        if(!TextUtils.isEmpty(desationUrl)) {
            Intent intent = new Intent(mContext, AdBrowserActivity.class);
            intent.putExtra(AdBrowserActivity.KEY_URL, mVideoInfo.clickUrl);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onClickbackBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onVideoLoadSuccess() {
        if(mSlotListener != null) {
            mSlotListener.onAdVideoLoadSuccess();
        }
    }

    @Override
    public void onVideoLoadFailed() {
        if(mSlotListener != null) {
            mSlotListener.onAdVideoLoadFailed();
        }

        //加载失败全部回到初始状态
        canPause = false;
    }

    @Override
    public void onVideoPlayComplete() {
        try {
            ReportManager.sueReport(mVideoInfo.endMonitor, false, getDuration());
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(mSlotListener != null) {
            mSlotListener.onAdVideoPlayComplete();
        }
        mVideoView.setIsPausedClicked(true);

    }

    private boolean isComplete() {
        if(mVideoView != null) {
            return mVideoView.isComplete();
        }
        return false;
    }

    private int getPosition() {
        return mVideoView.getCurrentPositon() / SDKConstants.MILLION_UNIT;
    }

    private int getDuration() {
        return mVideoView.getDuration() / SDKConstants.MILLION_UNIT;
    }

    private boolean isPlaying() {
        if(mVideoView != null) {
            return mVideoView.isPlaying();
        }
        return false;
    }

    private boolean isRealPause() {
        if(mVideoView != null) {
            return mVideoView.isRealPause();
        }
        return false;
    }

    private void resumeVideo() {
        if(mVideoView != null) {
            mVideoView.resume();
        }
    }

    private void pauseVideo() {
        if(mVideoView != null) {
            mVideoView.seekAndResume(0);
        }
    }

    //传递消息到appcontext层
    public interface AdSDKSlotListener {

        public ViewGroup getAdParent();

        public void onAdVideoLoadSuccess();

        public void onAdVideoLoadFailed();

        public void onAdVideoPlayComplete();

        public void onClickVideo(String url);
    }
}

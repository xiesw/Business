package com.halove.core.widget.video;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.halove.core.R;
import com.halove.core.entity.VideoVaule;
import com.halove.core.utils.LogUtil;

/**
 * Created by xieshangwu on 2017/8/5
 */

public class VideoFullDialog extends Dialog implements VideoPlayerListener {
    private static final String TAG = VideoFullDialog.class.getSimpleName();

    /**
     * ui
     */
    private CustomVideoView mVideoView;
    private ViewGroup mParentView;
    private ImageView mBackButton;

    private VideoVaule mVideoVaule;
    private FullToSmallListener mListener;
    private int mPositon;
    private boolean isFirst = true;

    public VideoFullDialog(@NonNull Context context, CustomVideoView videoView, VideoVaule
            videoVaule) {

        super(context, R.style.dialog_full_screen);
        mVideoVaule = videoVaule;
        mVideoView = videoView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.xadsdk_dialog_video_layout);
        initVideoView();
    }

    private void initVideoView() {
        mParentView = (RelativeLayout) findViewById(R.id.content_layout);
        mBackButton = (ImageView) findViewById(R.id.xadsdk_player_close_btn);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }
        });
        mVideoView.setListener(this);   //设置事件监听
        mVideoView.setMute(false);
        mParentView.addView(mVideoView);
    }

    /**
     * 全屏返回事件监听
     */
    private void onClickBackBtn() {
        dismiss();
        if(mListener != null) {
            mListener.getCurrentPlayPosition(mVideoView.getCurrentPositon());
        }
    }

    @Override
    public void onBackPressed() {
        onClickBackBtn();
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LogUtil.e(TAG, "onWindowFocusChanged");
        if(!hasFocus) {
            //未获取焦点
            mPositon = mVideoView.getCurrentPositon();
            mVideoView.pause();
        } else {
            // 首次创建获得焦点 , 机型适配
            if(isFirst) {
                mVideoView.seekAndResume(mPositon);
            } else {
                mVideoView.resume();
            }
            // 取得焦点, 恢复视频播放
            mVideoView.resume();
        }
    }

    @Override
    public void dismiss() {
        LogUtil.e(TAG, "dismiss");
        mParentView.removeView(mVideoView);
        super.dismiss();
    }

    @Override
    public void onBufferUpdate(int time) {

    }

    @Override
    public void onClickFullScreenBtn() {
        onClickVideo();
    }

    @Override
    public void onClickVideo() {

    }

    @Override
    public void onClickbackBtn() {

    }

    @Override
    public void onClickPlay() {

    }

    @Override
    public void onVideoLoadSuccess() {
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onVideoLoadFailed() {

    }

    /**
     * 小屏模式 单独处理
     */
    @Override
    public void onVideoPlayComplete() {
        dismiss();
        if(mListener != null) {
            mListener.playComplete();
        }
    }


    //设置监听
    public void setListener(FullToSmallListener listener) {
        mListener = listener;
    }

    /**
     * 也业务逻辑层进行通信
     */
    public interface FullToSmallListener {
        void getCurrentPlayPosition(int position);

        void playComplete();//全屏播放结束时回调
    }
}

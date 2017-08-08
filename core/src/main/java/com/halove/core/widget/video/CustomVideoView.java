package com.halove.core.widget.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.halove.core.R;
import com.halove.core.SDKConstants;
import com.halove.core.utils.LogUtil;
import com.halove.core.utils.Utils;

/**
 * Created by xieshangwu on 2017/8/4
 * @function 负责视频播放, 暂停以及各类的事件触发
 */

public class CustomVideoView extends RelativeLayout implements View.OnClickListener, MediaPlayer
        .OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, TextureView.SurfaceTextureListener, MediaPlayer
                .OnInfoListener {

    public static final String TAG = CustomVideoView.class.getSimpleName();

    /**
     * constants
     */
    public static final int TIME_MSG = 0x01;
    public static final int TIME_INVAL = 1000;
    //播放器生命周期状态
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSING = 2;
    //重试次数
    public static final int LOAD_TOTAL_COUNT = 3;

    /**
     * UI
     */
    private ViewGroup mParentContainer;
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private Button mMiniPlayBtn;
    private ImageView mFullBtn;
    private ImageView mLoadingBar;
    private ImageView mFrameView;
    private AudioManager mAudioManager; // 音量控制器
    private Surface videoSurface;       // 显示帧数据的类

    /**
     * Data
     */
    private String mUrl;
    private boolean isMute;
    private int mScreenWidth, mDestationHeight;

    /**
     * Status 状态保护
     */
    private boolean mIsRealPause;
    private boolean mIsComplete;
    private int mCurrentCount;
    private int mPlayerState = STATE_IDLE;   // 默认处于空闲状态

    private MediaPlayer mMediaPlayer;
    private VideoPlayerListener mListener;  // 事件监听回调
    private ScreenEventReceiver mScreenEventReceiver;   //监听是否锁屏

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case TIME_MSG:
                    // TODO: 2017/8/4
                    break;
                default:
                    break;
            }
        }
    };
    private boolean mIsPausedClicked;

    public CustomVideoView(Context context, ViewGroup parentContainer) {
        super(context);
        mParentContainer = parentContainer;
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
        registerBroadcastReceiver();
    }

    private void initView() {
        mPlayerView = (RelativeLayout) View.inflate(getContext(), R.layout.view_video_player, null);
        mVideoView = (TextureView) mPlayerView.findViewById(R.id.view_player_video_textureView);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);

        //LayoutParams params = new LayoutParams(mScreenWidth, mDestationHeight);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mScreenWidth,
                mDestationHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPlayerView.setLayoutParams(params);

        mMiniPlayBtn = (Button) mPlayerView.findViewById(R.id.view_small_play_btn);
        mFullBtn = (ImageView) mPlayerView.findViewById(R.id.view_to_full_view);
        mLoadingBar = (ImageView) mPlayerView.findViewById(R.id.loading_bar);
        mFrameView = (ImageView) mPlayerView.findViewById(R.id.framing_view);
        mFullBtn.setOnClickListener(this);
        mMiniPlayBtn.setOnClickListener(this);
    }

    private void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mDestationHeight = (int) (mScreenWidth * SDKConstants.VIDEO_HEIGHT_PERCENT);
    }

    public void setDataSource(String url) {
        this.mUrl = url;
    }

    /**
     *  加载我们的url
     */
    public void load() {
        if(mPlayerState != STATE_IDLE) {
            return;
        }

        try {
            showLoadingView();
            setCurrentPalyState(STATE_IDLE);
            checkMediaPlayer(); // 完成我们播放器创建
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync(); //异步加载视频
        } catch(Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    /**
     * 暂停视频
     */
    public void pause() {
        if(mPlayerState != STATE_PLAYING) {
            return;
        }

        setCurrentPalyState(STATE_PAUSING);
        if(isPlaying()) {
            mMediaPlayer.pause();
        }

        showPauseView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 恢复视频
     */
    public void resume() {
        if(mPlayerState != STATE_PAUSING) {
            return;
        }

        if(!isPlaying()) {
            entryResumeState(); //  值为播放状态值
            showPauseView(false);
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
        }
    }

    public boolean isPlaying() {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    /**
     * 播放完回调初始状态
     */
    public void playBack() {
        setCurrentPalyState(STATE_PAUSING);
        mHandler.removeCallbacksAndMessages(null);
        if(mMediaPlayer != null) {
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        }
        showPauseView(false);
    }

    /**
     * 停止状态
     */
    public void stop() {
        LogUtil.e(TAG, "do stop");
        // 清空
        if(mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mHandler.removeCallbacksAndMessages(null);
        setCurrentPalyState(STATE_IDLE);

        // 重连接
        if(mCurrentCount < LOAD_TOTAL_COUNT) {
            mCurrentCount++;
            load();
        } else {
            // 停止连接
            showPauseView(false);
        }
    }

    /**
     * 销毁我们的自定义view
     */
    public void distroy() {
        unRegisterBroadcastReceiver();
    }

    /**
     * 跳到指定点播放视频
     * @param position 跳转的位置
     */
    public void seekAndResume(int position) {
        if (this.mPlayerState != STATE_PLAYING) {
            return;
        }
        showPauseView(false);
        setCurrentPalyState(STATE_PAUSING);
        if (isPlaying()) {
            mMediaPlayer.seekTo(position);
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    LogUtil.d(TAG, "do seek and pause");
                    mMediaPlayer.pause();
                    mHandler.removeCallbacksAndMessages(null);
                }
            });
        }
    }

    public void setListener(VideoPlayerListener listener) {
        this.mListener = listener;
    }

    private synchronized void checkMediaPlayer() {
        if(mMediaPlayer == null) {
            mMediaPlayer = createMediaPlayer(); // 创建新的播放器
        }
    }

    private MediaPlayer createMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(videoSurface != null && videoSurface.isValid()) {
            mMediaPlayer.setSurface(videoSurface);
        } else {
            //stop();
        }
        return mMediaPlayer;
    }

    private void registerBroadcastReceiver() {
        if(mScreenEventReceiver == null) {
            mScreenEventReceiver = new ScreenEventReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            getContext().registerReceiver(mScreenEventReceiver, filter);
        }
    }


    private void unRegisterBroadcastReceiver() {
        if(mScreenEventReceiver != null) {
            getContext().unregisterReceiver(mScreenEventReceiver);
        }
    }

    /**
     * 判断是否能播放
     */
    private void decideCanPlay() {
        if (Utils.getVisiblePercent(mParentContainer) > SDKConstants.VIDEO_SCREEN_PERCENT)
            //来回切换页面时，只有 >50,且满足自动播放条件才自动播放
            resume();
        else
            pause();
    }

    public void isShowFullBtn(boolean isShow) {

    }

    public boolean isPauseBtnClicked() {
        return mIsRealPause;
    }

    public boolean isComplete() {
        return isComplete();
    }

    public boolean isRealPause() {
        return mIsRealPause;
    }

    public void setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }

    public void setIsRealPause(boolean b) {
        mIsRealPause = b;
    }

    public void setMute(boolean b) {
        isMute = b;
    }

    private void showPauseView(boolean show) {
        mFullBtn.setVisibility(show ? VISIBLE: GONE);
        mMiniPlayBtn.setVisibility(show ? VISIBLE: GONE);

        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(GONE);
        if(!show) {
            mFrameView.setVisibility(VISIBLE);
            loadFrameImage();
        } else {
            mFrameView.setVisibility(GONE);
        }
    }

    private void showLoadingView() {
        mFullBtn.setVisibility(GONE);
        mLoadingBar.setVisibility(VISIBLE);
        AnimationDrawable anim = (AnimationDrawable) mLoadingBar.getBackground();
        anim.start();
        mMiniPlayBtn.setVisibility(GONE);
        mFrameView.setVisibility(GONE);
        loadFrameImage();
    }

    private void showPlayView() {
        mLoadingBar.clearAnimation();
        mLoadingBar.setVisibility(View.GONE);
        mMiniPlayBtn.setVisibility(View.GONE);
        mFrameView.setVisibility(View.GONE);
    }

    /**
     * 异步加载定帧图
     */
    private void loadFrameImage() {
    }

    private void entryResumeState() {
        setCurrentPalyState(STATE_PLAYING);
        setIsPausedClicked(false);
        setIsComplete(false);
    }

    private void setCurrentPalyState(int state) {
        mPlayerState = state;
    }

    public int getDuration() {
        if(mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getCurrentPositon() {
        if(mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        LogUtil.e(TAG, "onVisibilityChanged" + visibility);
        super.onVisibilityChanged(changedView, visibility);

        if(visibility == VISIBLE && mPlayerState == STATE_PLAYING) {
            // 决定是否播放
            if(mIsRealPause || isComplete()) {
                // 表明播放器进入了真正的暂停
                pause();
            } else {
                decideCanPlay();
            }
        } else {
            pause();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    /**
     * 播放器播放完成回调
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mListener != null) {
            mListener.onVideoPlayComplete();
        }
        setIsComplete(true);
        setIsPausedClicked(true);

        playBack(); // 回到初始状态
    }

    /**
     * 播放器产生异常的回调
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtil.e(TAG, "error");
        this.mPlayerState = STATE_ERROR;
        if(mCurrentCount >= LOAD_TOTAL_COUNT) {
            if(mListener != null) {
                mListener.onVideoLoadFailed();
            }
            showPauseView(false);
        }
        stop();
        return true;
    }

    /**
     * 播放器处于就绪状态
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.e(TAG, "prepare");

        mMediaPlayer = mp;
        if(mMediaPlayer != null) {
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mCurrentCount = 0;
            if(mListener != null) {
                mListener.onVideoLoadSuccess();
            }
            decideCanPlay();
        }
    }

    /**
     * 表明我们的TextureView处于就绪状态
     * @param surface
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtil.e(TAG, "onSurfaceTextureAvailable");
        videoSurface = new Surface(surface);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtil.e(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onClick(View v) {

    }

    public void setIsPausedClicked(boolean isPausedClicked) {
        mIsPausedClicked = isPausedClicked;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        return false;
    }

    /**
     * 锁屏广播事件监听
     */
    public class ScreenEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch(intent.getAction()) {
                case Intent.ACTION_USER_PRESENT:
                    if(mPlayerState == STATE_PAUSING) {
                        if(mIsRealPause) {
                            // 播放结束, 依然暂停
                            pause();
                        }
                    } else {
                        decideCanPlay();
                    }
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    if(mPlayerState == STATE_PLAYING) {
                        pause();
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        @ViewDebug.ExportedProperty(
                category = "layout"
        )
        public boolean alignWithParent;

        public LayoutParams(Context c, AttributeSet attrs) {
            super((android.view.ViewGroup.LayoutParams)null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(int w, int h) {
            super((android.view.ViewGroup.LayoutParams)null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super((android.view.ViewGroup.LayoutParams)null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(MarginLayoutParams source) {
            super((android.view.ViewGroup.LayoutParams)null);
            throw new RuntimeException("Stub!");
        }

        public LayoutParams(RelativeLayout.LayoutParams source) {
            super((android.view.ViewGroup.LayoutParams)null);
            throw new RuntimeException("Stub!");
        }

        public String debug(String output) {
            throw new RuntimeException("Stub!");
        }

        public void addRule(int verb) {
            throw new RuntimeException("Stub!");
        }

        public void addRule(int verb, int anchor) {
            throw new RuntimeException("Stub!");
        }

        public void removeRule(int verb) {
            throw new RuntimeException("Stub!");
        }

        public int getRule(int verb) {
            throw new RuntimeException("Stub!");
        }

        public int[] getRules() {
            throw new RuntimeException("Stub!");
        }

        public void resolveLayoutDirection(int layoutDirection) {
            throw new RuntimeException("Stub!");
        }
    }
}
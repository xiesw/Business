package com.halove.core.widget.video.sdk;

import android.view.ViewGroup;

import com.halove.core.entity.VideoVaule;
import com.halove.core.utils.ResponseEntityToModule;
import com.halove.core.widget.video.FrameImageLoadListener;
import com.halove.core.widget.video.VideoSlot;

/**
 * Created by xieshangwu on 2017/8/6
 * @function 管理slot, 数据校验
 */

public class VideoSDK  {
    public static final String TAG = VideoSDK.class.getSimpleName();

    private ViewGroup mParentView;

    private VideoSlot mVideoSlot;
    private VideoVaule mVideoVaule;

    private VideoSdkInterface mListener;
    private FrameImageLoadListener mFrameImageLoadListener;

    public VideoSDK(ViewGroup parentView, String instance, FrameImageLoadListener frameImageLoadListener) {
        mParentView = parentView;
        mFrameImageLoadListener = frameImageLoadListener;
        this.mVideoVaule = (VideoVaule) ResponseEntityToModule.
                parseJsonToModule(instance, VideoVaule.class);

    }

}

package com.halove.core.widget.video;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by xieshangwu on 2017/8/4
 */

public interface FrameImageLoadListener {

    void onStartFrameLoad(String url, ImageLoadingListener listener);
}

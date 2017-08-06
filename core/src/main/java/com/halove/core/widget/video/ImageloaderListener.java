package com.halove.core.widget.video;

import android.graphics.Bitmap;

/**
 * Created by xieshangwu on 2017/8/4
 */

public interface ImageloaderListener {

    // 下载不成功  传null
    void onLoadingComplete(Bitmap loadedimage);
}

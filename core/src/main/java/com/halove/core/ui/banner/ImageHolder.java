package com.halove.core.ui.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.halove.core.imageloader.ImageLoaderManager;

/**
 * Created by xieshangwu on 2017/7/24
 */

public class ImageHolder implements Holder<String> {


    private AppCompatImageView mImageView;

    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int i, String s) {
        ImageLoaderManager.getInstance(context).displayImage(s, mImageView);
    }
}

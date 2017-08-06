package com.halove.business.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.halove.core.imageloader.ImageLoaderManager;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by xieshangwu on 2017/8/6
 */

public class PhotoViewAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mPhotoList;

    public PhotoViewAdapter(Context context, ArrayList<String> photoLists) {
        mContext = context;
        mPhotoList = photoLists;
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView photoView = new PhotoView(mContext);
        ImageLoaderManager.getInstance(mContext).displayImage(mPhotoList.get(position),photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);
        return photoView;
    }
}

package com.halove.business.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.ui.adapter.PhotoViewAdapter;
import com.halove.business.base.BaseActivity;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/2
 *
 * @function 显示产品大图
 */
public class PhotoViewActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String TAG = PhotoViewActivity.class.getSimpleName();
    public static final String PHOTO_LIST = "photo_list";
    protected ViewPager mPhotoPager;
    protected TextView mIndictoreView;
    private ImageView mShareView;

    private PhotoViewAdapter mViewAdapter;
    private ArrayList<String> mPhotoLists;
    private int mLenght;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    private void initData() {
        mPhotoLists = getIntent().getStringArrayListExtra(PHOTO_LIST);
        mLenght = mPhotoLists.size();

        mViewAdapter = new PhotoViewAdapter(this, mPhotoLists);
        mPhotoPager.setAdapter(mViewAdapter);
        mPhotoPager.setOnPageChangeListener(this);
        onPageScrolled(1, 0, 0);
    }

    private void initView() {
        mPhotoPager = (ViewPager) findViewById(R.id.photo_view_pager);
        mIndictoreView = (TextView) findViewById(R.id.indictore_view);
        mShareView = (ImageView) findViewById(R.id.share_view);
        mShareView.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int tatol = mPhotoLists.size();
        String indictorText = (position + 1) + " / " + (tatol);
        mIndictoreView.setText(indictorText);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }
}

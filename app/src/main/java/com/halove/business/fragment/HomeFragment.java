package com.halove.business.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.halove.business.Constant;
import com.halove.business.R;
import com.halove.business.activity.PhotoViewActivity;
import com.halove.business.adapter.HomeListAdapter;
import com.halove.business.base.BaseFragment;
import com.halove.business.entity.recommand.BaseRecommandEntity;
import com.halove.business.entity.recommand.HeadEntity;
import com.halove.business.entity.recommand.RecommandValue;
import com.halove.business.net.http.RequestCenter;
import com.halove.business.zxing.app.CaptureActivity;
import com.halove.core.okhttp.listener.DisposeDataListener;
import com.halove.core.ui.banner.BannerCreator;

import java.util.ArrayList;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView
        .OnItemClickListener {
    private static final String TAG = BaseFragment.class.getSimpleName();

    public static final int REQUEST_QRCORD = 0x01;

    /**
     * ui
     */
    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    private BaseRecommandEntity mBaseRecommandEntity;
    private HomeListAdapter mHomeListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        // 启动loading动画
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.qrcode_view:
                if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doOpenCamera();
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
                break;
        }
    }

    public void doOpenCamera() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_QRCORD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_QRCORD:
                // 扫码处理逻辑
                if(resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra("SCAN_RESULT");
                    // TODO: 2017/8/4 使用浏览器打开返回的连接
                }
                break;
            default:
                break;
        }
    }

    // 推荐产品数据请求
    private void requestRecommandData() {
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                // 完成真正的逻辑
                if(responseObj instanceof BaseRecommandEntity) {
                    mBaseRecommandEntity = (BaseRecommandEntity) responseObj;
                    showSuccessView();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                // 提示用户网络问题
            }
        });
    }

    /**
     * 请求成功显示的view
     */
    private void showSuccessView() {
        if(mBaseRecommandEntity.data.list != null && mBaseRecommandEntity.data.list.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            // 创建adapter
            mHomeListAdapter = new HomeListAdapter(getActivity(), mBaseRecommandEntity.data.list);
            mListView.setAdapter(mHomeListAdapter);

            addHead();
        } else {
            showErrorView();
        }
    }

    /**
     * 添加头部ViewPager
     */
    @SuppressWarnings("unchecked")
    private void addHead() {
        if(mBaseRecommandEntity.data.head != null) {
            HeadEntity headEntity = mBaseRecommandEntity.data.head;
            ArrayList<String> urlList = headEntity.ads;
            View headView = View.inflate(getActivity(), R.layout.item_home_head, null);
            ConvenientBanner<String> convenientBanner = (ConvenientBanner<String>) headView
                    .findViewById(R.id.banner_recycler_item);
            BannerCreator.setDefault(convenientBanner, urlList, null);
            mListView.addHeaderView(headView);

        }
    }

    /**
     * 请求失败显示的view
     */
    private void showErrorView() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecommandValue value = mHomeListAdapter.getItem(position - mListView.getHeaderViewsCount());
        if(value.type != 0) {
            Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            startActivity(intent);

        }
    }
}

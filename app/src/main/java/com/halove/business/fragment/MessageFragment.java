package com.halove.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.base.BaseFragment;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class MessageFragment extends BaseFragment {
    private static final String TAG = MessageFragment.class.getSimpleName();

    private View mContentView;
    private TextView mMessageView;
    private TextView mZanView;
    private TextView mImoocView;
    private TextView mTipView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_message, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mMessageView = (TextView) mContentView.findViewById(R.id.tip_message_view);
        mImoocView = (TextView) mContentView.findViewById(R.id.tip_imooc_view);
        mZanView = (TextView) mContentView.findViewById(R.id.zan_message_info_view);
        mTipView = (TextView) mContentView.findViewById(R.id.tip_view);
    }
}

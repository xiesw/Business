package com.halove.business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halove.business.R;
import com.halove.business.base.BaseFragment;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class HomeFragment extends BaseFragment {

    private View mContentView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_home, container, false);
        return mContentView;
    }
}

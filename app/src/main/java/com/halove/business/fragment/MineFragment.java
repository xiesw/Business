package com.halove.business.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.activity.SettingActivity;
import com.halove.business.base.BaseFragment;
import com.halove.business.entity.update.UpdateEntity;
import com.halove.business.net.http.RequestCenter;
import com.halove.business.service.UpdateService;
import com.halove.business.ui.Dialog.CommonDialog;
import com.halove.business.zxing.util.Util;
import com.halove.core.okhttp.listener.DisposeDataListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MineFragment.class.getSimpleName();

    private View mContentView;
    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mVideoPlayerView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mLoginLayout = (RelativeLayout) mContentView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mLoginedLayout = (RelativeLayout) mContentView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);

        mPhotoView = (CircleImageView) mContentView.findViewById(R.id.photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = (TextView) mContentView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);
        mVideoPlayerView = (TextView) mContentView.findViewById(R.id.video_setting_view);
        mVideoPlayerView.setOnClickListener(this);
        mShareView = (TextView) mContentView.findViewById(R.id.share_imooc_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = (TextView) mContentView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mLoginInfoView = (TextView) mContentView.findViewById(R.id.login_info_view);
        mUserNameView = (TextView) mContentView.findViewById(R.id.username_view);
        mTickView = (TextView) mContentView.findViewById(R.id.tick_view);

        mUpdateView = (TextView) mContentView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.video_setting_view:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.update_view:
                checkVersion();
                break;
            default:
                break;
        }
    }

    // 发送版本检查请求
    private void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UpdateEntity updateEntity = (UpdateEntity) responseObj;
                // 判断版本号大小
                if(Util.getVersionCode(getActivity()) < updateEntity.data.currentVersion) {
                    CommonDialog dialog = new CommonDialog(getActivity(), getString(R.string
                            .update_new_version), getString(R.string.update_title), getString(R
                            .string.update_install), getString(R.string.cancel), new CommonDialog
                            .DialogClickListener() {


                        @Override
                        public void onDialogClick() {
                            Intent intent = new Intent(getActivity(), UpdateService.class);
                            getActivity().startService(intent);
                        }
                    });
                    dialog.show();
                } else {
                    CommonDialog dialog = new CommonDialog(getActivity(), getString(R.string
                            .update_new_version), getString(R.string.update_title), getString(R
                            .string.update_install), getString(R.string.button_ok), null);
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

}

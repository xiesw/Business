package com.halove.business.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.halove.business.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by xieshangwu on 2017/8/7
 * 底部分享对话框
 */

public class ShareDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = ShareDialog.class.getSimpleName();
    private Context mContext;
    private DisplayMetrics dm;
    /**
     * UI
     */
    private RelativeLayout mWeixinLayout;
    private RelativeLayout mWeixinMomentLayout;
    private RelativeLayout mQQLayout;
    private RelativeLayout mQZoneLayout;
    private TextView mCancelView;

    /**
     * 分享的数据类型
     */
    private int mShareType;
    private String mShareTitle;
    private String mSharePhoto;
    private String mShareText;
    private String mShareTitleUrl;
    private String mShareSite;
    private String mShareSiteUrl;
    private String mUrl;

    private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.SheetDialogStyle);
        mContext = context;
        dm = mContext.getResources().getDisplayMetrics();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        initView();
    }

    private void initView() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        dialogWindow.setAttributes(lp);

        mWeixinLayout = (RelativeLayout) findViewById(R.id.weixin_layout);
        mWeixinLayout.setOnClickListener(this);
        mWeixinMomentLayout = (RelativeLayout) findViewById(R.id.moment_layout);
        mWeixinMomentLayout.setOnClickListener(this);
        mQQLayout = (RelativeLayout) findViewById(R.id.qq_layout);
        mQQLayout.setOnClickListener(this);
        mQZoneLayout = (RelativeLayout) findViewById(R.id.qzone_layout);
        mQZoneLayout.setOnClickListener(this);
        mCancelView = (TextView) findViewById(R.id.cancel_view);
        mCancelView.setOnClickListener(this);
    }

    public void setShareText(String shareText) {
        mShareText = shareText;
    }

    public void setShareType(int shareType) {
        mShareType = shareType;
    }

    public void setShareTitle(String shareTitle) {
        mShareTitle = shareTitle;
    }

    public void setSharePhoto(String sharePhoto) {
        mSharePhoto = sharePhoto;
    }

    public void setShareTitleUrl(String shareTitleUrl) {
        mShareTitleUrl = shareTitleUrl;
    }

    public void setShareSite(String shareSite) {
        mShareSite = shareSite;
    }

    public void setShareSiteUrl(String shareSiteUrl) {
        mShareSiteUrl = shareSiteUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.weixin_layout:

                break;
            case R.id.moment_layout:

                break;
            case R.id.qq_layout:
                share(ShareManager.PlatformType.QQ);
                break;
            case R.id.cancel_view:
                dismiss();
                break;

            default:
                break;
        }
    }

    // 完成分享的方法
    public void share(ShareManager.PlatformType type) {
        // 封装我的ShareData
        ShareData data = new ShareData();
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(mShareType);
        params.setTitle(mShareTitle);
        params.setTitleUrl(mShareTitleUrl);
        params.setSite(mShareSite);
        params.setSiteUrl(mShareSiteUrl);
        params.setText(mShareText);
        params.setImagePath(mSharePhoto);
        params.setUrl(mUrl);

        data.mParams = params;
        data.type = type;
        ShareManager.getInstance().shareData(data, mPlatformActionListener);
    }
}

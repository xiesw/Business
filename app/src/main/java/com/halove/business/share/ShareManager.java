package com.halove.business.share;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

import static com.halove.business.share.ShareManager.PlatformType.QZone;
import static com.halove.business.share.ShareManager.PlatformType.Wechat;
import static com.halove.business.share.ShareManager.PlatformType.WechatMoment;

/**
 * Created by xieshangwu on 2017/8/7
 *
 * @function 分享功能入口
 */

public class ShareManager {
    public static final String TAG = ShareManager.class.getSimpleName();
    private Platform mCurrentPlatform; //当前平台类型
    private ShareManager() {

    }

    private static final class Holder {
        private static ShareManager instance = new ShareManager();
    }

    public static ShareManager getInstance() {
        return Holder.instance;
    }

    public static void init(Context context) {
        ShareSDK.initSDK(context);
    }

    public void shareData(ShareData data, PlatformActionListener listener) {
        switch(data.type) {
            case QQ:
                mCurrentPlatform = ShareSDK.getPlatform(QQ.NAME);
                break;

            case QZone:
                mCurrentPlatform = ShareSDK.getPlatform(QZone.name());
                break;

            case Wechat:
                mCurrentPlatform = ShareSDK.getPlatform(Wechat.name());
                break;

            case WechatMoment:
                mCurrentPlatform = ShareSDK.getPlatform(WechatMoment.name());
                 break;
            default:
                break;
        }
        mCurrentPlatform.setPlatformActionListener(listener);
        mCurrentPlatform.share(data.mParams);
    }

    public enum PlatformType {
        QQ, QZone, Wechat, WechatMoment
    }

}

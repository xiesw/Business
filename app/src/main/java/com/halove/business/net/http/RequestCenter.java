package com.halove.business.net.http;

import com.halove.business.entity.UserEntity;
import com.halove.business.entity.recommand.BaseRecommandEntity;
import com.halove.business.entity.update.UpdateEntity;
import com.halove.core.okhttp.CommonOkhttpClient;
import com.halove.core.okhttp.listener.DisposeDataHandle;
import com.halove.core.okhttp.listener.DisposeDataListener;
import com.halove.core.okhttp.request.CommonRequest;
import com.halove.core.okhttp.request.RequestParams;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 所有网络请求
 */

public class RequestCenter {

    private static void postRequest(String url, RequestParams params, DisposeDataListener
            listener, Class<?> clazz) {
        CommonOkhttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 首页请求
     * @param listener
     */
    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.HOME_RECOMMAND, null, listener,
                BaseRecommandEntity.class);
    }

    /**
     * 应用版本请求
     */
    public static void checkVersion(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.CHECK_UPDATE, null, listener, UpdateEntity.class);
    }

    /**
     * 登陆请求
     */
    public static void login(String userName, String passwd, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("username", userName);
        params.put("password", passwd);
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, UserEntity.class);
    }
}

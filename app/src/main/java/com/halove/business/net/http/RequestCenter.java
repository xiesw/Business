package com.halove.business.net.http;

import com.halove.business.entity.recommand.BaseRecommandEntity;
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
}

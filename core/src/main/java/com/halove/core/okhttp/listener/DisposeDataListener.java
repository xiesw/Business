package com.halove.core.okhttp.listener;

/**
 * Created by xieshangwu on 2017/8/3
 * 自定义事件监听
 */

public interface DisposeDataListener {

    /**
     * 请求成功回调处理
     */
    void onSuccess(Object responseObj);


    /**
     * 请求失败的回调处理
     */
    void onFailure(Object reasonObj);
}

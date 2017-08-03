package com.halove.core.okhttp;

import com.halove.core.okhttp.https.HttpsUtils;
import com.halove.core.okhttp.response.CommonJsonCallback;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 请求的发送, 请求参数的配置, https支持.
 */

public class CommonOkhttpClient {

    private static final int TIME_OUT = 30; //超时参数
    private static OkHttpClient mOkHttpClient;

    // 为我们的client去配置参数
    static {
        // 创建client的构建者
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        // 设置超时参数
        okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        // 允许转发重定向
        okHttpBuilder.followRedirects(true);

        // https支持
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        okHttpBuilder.sslSocketFactory(HttpsUtils.getSslSocketFactory());

        // 生成我们的client对象
        mOkHttpClient = okHttpBuilder.build();
    }

    /**
     * 发送具体的http/https请求
     * @param request
     * @param commonJsonCallback
     * @return Call
     */
    public static Call sendRequset(Request request, CommonJsonCallback commonJsonCallback) {

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonJsonCallback);

        return call;
    }

}

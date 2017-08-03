package com.halove.core.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.halove.core.okhttp.CommonOkhttpClient;
import com.halove.core.okhttp.listener.DisposeDataHandle;
import com.halove.core.okhttp.listener.DisposeDataListener;
import com.halove.core.okhttp.request.CommonRequest;
import com.halove.core.okhttp.response.CommonJsonCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class BaseOkhttpTestActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle
            persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    private void sendRequest() {

        // 创建okhttp对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建request
        final Request request = new Request.Builder().url("https://www.imooc.com/").build();
        // new call
        Call call = okHttpClient.newCall(request);
        // 请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void test() {
        CommonOkhttpClient.sendRequset(CommonRequest.createGetRequest("https://www.imooc.com",
                null), new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener() {


            @Override
            public void onSuccess(Object responseObj) {

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        })));
    }
}

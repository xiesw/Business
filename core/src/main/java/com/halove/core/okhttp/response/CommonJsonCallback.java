package com.halove.core.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.halove.core.okhttp.exception.OkHttpException;
import com.halove.core.okhttp.listener.DisposeDataHandle;
import com.halove.core.okhttp.listener.DisposeDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by xieshangwu on 2017/8/3\
 *
 * @function 专门处理JSON回调响应
 */

public class CommonJsonCallback implements Callback {

    // 与服务器返回的字段的一个对应关系
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的,但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    // 自定义异常类型
    protected final int NETWORK_ERROR = -1;
    protected final int JSON_ERROR = -2;
    protected final int OTHER_ERROR = -3;

    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        mDeliveryHandler = new Handler(Looper.getMainLooper());
        mListener = handle.mListener;
        mClass = handle.mClass;
    }

    /**
     * 请求失败处理,根据不同异常进行不同处理
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    /**
     * 真正响应处理函数
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    /**
     * 处理服务器返回的响应数据
     */
    private void handleResponse(Object responseObj) {
        if(responseObj != null && responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            JSONObject result = new JSONObject(responseObj.toString());

            // 可以尝试解析json
            if(result.has(RESULT_CODE)) {
                // 从json中取出响应码, 若为0, 则是正确的响应.
                if(result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if(mClass == null) {
                        mListener.onSuccess(responseObj);
                    } else {
                        //自己将json对象转化测实体对象
                        Gson gson = new Gson();
                        Object obj = gson.fromJson(responseObj.toString(), mClass);
                        if(obj != null) {
                            mListener.onSuccess(obj);
                        } else {
                            // 返回不是合法的json
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        }
                    }
                } else {
                    // 将服务器返回的异常回调到应用去处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR, result.get(RESULT_CODE)));
                }
            }
        } catch(JSONException e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
        }
    }
}

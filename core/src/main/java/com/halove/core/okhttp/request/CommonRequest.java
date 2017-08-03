package com.halove.core.okhttp.request;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 接受请求参数, 为我们生成Request对象
 */

public class CommonRequest {

    /**
     *
     * @param url
     * @param params
     * @return 返回一个创建好的的post Request
     */
    public static Request createPostRequest(String url, RequestParams params) {
        FormBody.Builder fromBodyBuild = new FormBody.Builder();
        if(params != null) {
            for(Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 将请求参数遍历添加到我们的请求构件中
                fromBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        // 通过请求构建类build方法获取真正的请求对象
        FormBody formBody = fromBodyBuild.build();

        return new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
    }

    /**
     *
     * @param url
     * @param params
     * @return 返回一个创建好的的get Request
     */
    public static Request createGetRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if(params != null) {
            for(Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 将请求参数遍历添加到我们的请求构件中
                urlBuilder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }

        return new Request.Builder()
                .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .build();
    }

}

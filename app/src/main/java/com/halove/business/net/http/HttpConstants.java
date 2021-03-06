package com.halove.business.net.http;

/**
 * Created by xieshangwu on 2017/8/3
 * @function 定义所有请求地址
 */

public class HttpConstants {

    /**
     * 服务器地址
     */
    private static final String ROOT_URL = "http://121.42.181.106:8080/examples/business/";

    //首页产品的请求地址
    public static final String HOME_RECOMMAND = ROOT_URL + "home_data.json";

    //首页产品的请求地址
    public static final String CHECK_UPDATE = ROOT_URL + "update.json";

    //用户信息的请求地址
    public static final String LOGIN = ROOT_URL + "user_info.json";
}

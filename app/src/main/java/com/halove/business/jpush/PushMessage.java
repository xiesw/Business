package com.halove.business.jpush;

import java.io.Serializable;

/**
 * Created by xieshangwu on 2017/8/7
 * @functon 极光推送消息实体, 包含所有的数据字段
 */

public class PushMessage implements Serializable{

    // 消息类型, 类型为2 需要登录
    public String messageType = null;

    // 连接,需要打开的Url地址
    public String messageUrl = null;

    // 详情内容, 需要消息推送页面显示的text
    public String messageContent = null;
}

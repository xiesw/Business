package com.halove.business.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.halove.business.ui.activity.HomeActivity;
import com.halove.business.ui.activity.LoginActivity;
import com.halove.business.manager.UserManager;
import com.halove.core.utils.ResponseEntityToModule;
import com.halove.core.utils.Utils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by xieshangwu on 2017/8/7
 * @function 用来接受极光推送
 */

public class JPushReceiver extends BroadcastReceiver{
    public static final String TAG = JPushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("xieshangwu", "receiver --------");
        Bundle bundle = intent.getExtras();
        if(intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            // 不需要跳转
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
        } else if(intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            // 需要跳转的Action
            PushMessage message = (PushMessage) ResponseEntityToModule.parseJsonToModule(bundle
                    .getString(JPushInterface.EXTRA_EXTRA), PushMessage.class);
            if(Utils.getCurrentTask(context)) {
                Intent pushIntent = new Intent();
                pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pushIntent.putExtra("pushMessage", message);
                // 表明应用启动
                if(message != null && message.messageType.equals("2") && !UserManager.getInstance()
                        .hasLogin()) {
                    // 需要登录 并且 当前未启动
                    pushIntent.setClass(context, LoginActivity.class);
                    pushIntent.putExtra("fromPush", true);
                } else {
                    // 用户登录, 直接跳到消息页面
                    pushIntent.setClass(context, PushMessgeActivity.class);
                }
                context.startActivity(pushIntent);
            } else {
                // 应用未启动
                Intent mainIntent = new Intent(context, HomeActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(message != null && message.messageType.equals("2")) {
                    // 跳到登录页面
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    loginIntent.putExtra("fromPush", true);
                    loginIntent.putExtra("pushMessage", message);
                    context.startActivities(new Intent[]{mainIntent, loginIntent});
                } else {
                    Intent pushIntent = new Intent(context, PushMessgeActivity.class);
                    pushIntent.putExtra("pushMessage", message);
                    context.startActivities(new Intent[]{pushIntent, mainIntent});
                }
            }
        }
    }
}

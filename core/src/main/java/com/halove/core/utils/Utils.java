package com.halove.core.utils;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.halove.core.SDKConstants;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author qndroid
 */
public class Utils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }

    public static int getVisiblePercent(View pView) {
        if(pView != null && pView.isShown()) {
            DisplayMetrics displayMetrics = pView.getContext().getResources().getDisplayMetrics();
            int displayWidth = displayMetrics.widthPixels;
            Rect rect = new Rect();
            pView.getGlobalVisibleRect(rect);
            if((rect.top > 0) && (rect.left < displayWidth)) {
                double areaVisible = rect.width() * rect.height();
                double areaTotal = pView.getWidth() * pView.getHeight();
                return (int) ((areaVisible / areaTotal) * 100);
            } else {
                return -1;
            }
        }
        return -1;
    }

    //is wifi connected
    public static boolean isWifiConnected(Context context) {
        if(context.checkCallingOrSelfPermission(permission.ACCESS_WIFI_STATE) != PackageManager
                .PERMISSION_GRANTED) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    //decide can autoplay the ad
    public static boolean canAutoPlay(Context context, SDKConstants.AutoPlaySetting setting) {
        boolean result = true;
        switch(setting) {
            case AUTO_PLAY_3G_4G_WIFI:
                result = true;
                break;
            case AUTO_PLAY_ONLY_WIFI:
                if(isWifiConnected(context)) {
                    result = true;
                } else {
                    result = false;
                }
                break;
            case AUTO_PLAY_NEVER:
                result = false;
                break;
        }
        return result;
    }

    public static String getAppVersion(Context context) {
        String version = "1.0.0"; //默认1.0.0版本
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch(Exception e) {
        }

        return version;
    }

    /**
     * 将数组中的所有素材IE拼接起来，空则拼接“”
     *
     * @param values
     * @return
     */
   /* public static String getAdIE(ArrayList<AdValue> values) {
        StringBuilder result = new StringBuilder();
        if(values != null && values.size() > 0) {
            for(AdValue value : values) {
                result.append(value.adid.equals("") ? "" : value.adid).append(",");
            }
            return result.substring(0, result.length() - 1);
        }
        return "";
    }*/

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        if(windowManager == null) {
            return displayMetrics;
        }
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = getDisplayMetrics(context);
        int displayWidth = displayMetrics.widthPixels;
        return displayWidth;
    }

    public static BitmapDrawable decodeImage(String base64drawable) {
        byte[] rawImageData = Base64.decode(base64drawable, 0);
        return new BitmapDrawable(null, new ByteArrayInputStream(rawImageData));
    }

    public static boolean isPad(Context context) {

        //如果能打电话那可能是平板或手机，再根据配置判断
        if(canTelephone(context)) {
            //能打电话可能是手机也可能是平板
            return (context.getResources().getConfiguration().
                    screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration
                    .SCREENLAYOUT_SIZE_LARGE;
        } else {
            return true; //不能打电话一定是平板
        }
    }

    private static boolean canTelephone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) ? false : true;
    }

    public static boolean containString(String source, String destation) {

        if(source.equals("") || destation.equals("")) {
            return false;
        }

        if(source.contains(destation)) {
            return true;
        }
        return false;
    }

    /**
     * 通过指定包名判断程序是否正在运行
     * @param context
     * @return
     */
    public static boolean getCurrentTask(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> appProcessInfos = activityManager.getRunningTasks(50);
        for (RunningTaskInfo process : appProcessInfos) {

            if (process.baseActivity.getPackageName().equals(context.getPackageName())
                    || process.topActivity.getPackageName().equals(context.getPackageName())) {

                return true;
            }
        }
        return false;
    }
}

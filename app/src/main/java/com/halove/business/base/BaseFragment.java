package com.halove.business.base;


import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Created by xieshangwu on 2017/8/2
 */

public class BaseFragment extends Fragment {
    public static final String TAG = null;
    public static final int EXTERNAL_STORAGE_PERIMSSION = 1;
    public static final int WRITE_CONTACTS = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for(String permisson : permissions) {
            if(ContextCompat.checkSelfPermission(getActivity(), permisson) != PackageManager
                    .PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch(requestCode) {
            case EXTERNAL_STORAGE_PERIMSSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    doSDCard();
                }
                break;
            case WRITE_CONTACTS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED) {
                    doWriteContants();
                }
                break;
            default:
                break;
        }
    }

    public void doWriteContants() {

    }

    // 读写sd卡权限
    public void doSDCard() {

    }
}

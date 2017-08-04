package com.halove.core.okhttp.listener;

/**
 * Created by xieshangwu on 2017/8/3
 */

public class DisposeDataHandle {

    public DisposeDataListener mListener;
    public Class<?> mClass;

    public DisposeDataHandle(DisposeDataListener listener) {
        mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        mListener = listener;
        mClass = clazz;
    }
}

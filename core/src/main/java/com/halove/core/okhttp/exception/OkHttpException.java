package com.halove.core.okhttp.exception;

/**
 * Created by xieshangwu on 2017/8/3
 * 自定义异常类
 */

public class OkHttpException extends Exception {
    public static final long serialVersionUID = 1L;

    private int ecode;

    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getECode() {
        return ecode;
    }
}

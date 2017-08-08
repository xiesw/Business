package com.halove.business.entity.update;

import com.halove.business.entity.BaseEntity;

/**
 * Created by xieshangwu on 2017/8/6
 */

public class UpdateEntity extends BaseEntity {

    public int ecode;
    public String emsg;
    public DataBean data;

    public static class DataBean {

        public int currentVersion;
        public String apkurl;

        @Override
        public String toString() {
            return "DataBean{" + "currentVersion=" + currentVersion + ", apkurl='" + apkurl + '\'' + '}';
        }
    }

    @Override
    public String toString() {
        return "UpdateEntity{" + "ecode=" + ecode + ", emsg='" + emsg + '\'' + ", data=" + data + '}';
    }
}

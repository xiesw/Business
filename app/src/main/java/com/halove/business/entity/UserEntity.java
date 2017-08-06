package com.halove.business.entity;

/**
 * Created by xieshangwu on 2017/8/6
 * 用户信息
 */

public class UserEntity extends BaseEntity {

    public int ecode;
    public String emsg;
    public DataBean data;

    public static class DataBean {

        public String userId;
        public String photoUrl;
        public String name;
        public String tick;
        public String mobile;
        public String platform;

    }
}

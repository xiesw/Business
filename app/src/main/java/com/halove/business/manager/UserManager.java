package com.halove.business.manager;

import com.halove.business.entity.UserEntity;

/**
 * Created by xieshangwu on 2017/8/
 *
 * @function 单例管理登陆用户信息
 */

public class UserManager {

    private UserEntity mUserInfo;

    private static final class Holder {
        private static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance() {
        return Holder.INSTANCE;
    }

    public UserEntity getUser() {
        return mUserInfo;
    }

    public void setUser(UserEntity userEntity) {
        mUserInfo = userEntity;
    }

    public void removeUser() {
        mUserInfo = null;
    }

    @SuppressWarnings("RedundantConditionalExpression")
    public boolean hasLogin() {
        return (mUserInfo != null) ? true : false;
    }
}

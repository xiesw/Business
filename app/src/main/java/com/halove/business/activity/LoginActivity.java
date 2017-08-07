package com.halove.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.business.entity.UserEntity;
import com.halove.business.jpush.PushMessage;
import com.halove.business.jpush.PushMessgeActivity;
import com.halove.business.manager.UserManager;
import com.halove.business.net.http.RequestCenter;
import com.halove.core.okhttp.listener.DisposeDataListener;
import com.halove.core.utils.LogUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String LOGIN_ACTION = "com.halove.action.ACTION_LOGIN";

    /**
     * UI
     */
    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mLoginView;


    /**
     * data
     */

    private PushMessage mPushMessage;
    private boolean mFromPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if(intent.hasExtra("pushMessage")) {
            mPushMessage = (PushMessage) intent.getSerializableExtra("pushMessage");
        }
        mFromPush = intent.getBooleanExtra("fromPush", false);
    }

    private void initView() {

        mUserNameView = (EditText) findViewById(R.id.login_input_username);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mLoginView = (TextView) findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                login();
                 break;
            default:
                 break;
        }
    }

    //发送登陆请求
    private void login() {
        // TODO: 2017/8/6 数据校验
        String userName = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();

        //发送登陆请求
        RequestCenter.login(userName, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                LogUtil.ee(TAG, "success");

                // 拿到用户信息
                UserEntity user = (UserEntity) responseObj;
                UserManager.getInstance().setUser(user);
                // 发送广播
                seedLoginBroadcast();
                if(mFromPush) {
                    Intent intent = new Intent(LoginActivity.this, PushMessgeActivity.class);
                    intent.putExtra("pushMessage", mPushMessage);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onFailure(Object reasonObj) {
                LogUtil.ee(TAG, "fail");

            }
        });
    }

    /**
     * 发送登陆广播
     */
    private void seedLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGIN_ACTION));
    }
}

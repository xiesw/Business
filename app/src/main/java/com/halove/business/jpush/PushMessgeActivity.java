package com.halove.business.jpush;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.halove.business.R;
import com.halove.core.activity.AdBrowserActivity;

public class PushMessgeActivity extends AppCompatActivity {

    private static final String TAG = PushMessgeActivity.class.getSimpleName();
    /**
     * UI
     */
    private TextView mTypeView;
    private TextView mTypeValueView;
    private TextView mContentView;
    private TextView mContentValueView;

    /**
     * data
     */
    private PushMessage mPushMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushmessge);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mPushMessage = (PushMessage) intent.getSerializableExtra("pushMessage");

        mTypeValueView.setText(mPushMessage.messageType);
        mContentValueView.setText(mPushMessage.messageContent);
        if(!TextUtils.isEmpty(mPushMessage.messageUrl)) {
            gotoWebView();
        }
    }

    private void gotoWebView() {
        Intent intent = new Intent(this, AdBrowserActivity.class);
        intent.putExtra(AdBrowserActivity.KEY_URL, mPushMessage.messageUrl);
        startActivity(intent);
        finish();
    }

    private void initView() {
        mTypeView = (TextView) findViewById(R.id.message_type_view);
        mTypeValueView = (TextView) findViewById(R.id.message_type_value_view);
        mContentView = (TextView) findViewById(R.id.message_content_view);
        mContentValueView = (TextView) findViewById(R.id.message_content_value_view);
    }
}

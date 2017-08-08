package com.halove.business.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.halove.business.R;
import com.halove.core.widget.video.CustomVideoView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_test);
        CustomVideoView videoView = new CustomVideoView(this, relativeLayout);

        videoView.setDataSource("http://121.42.181.106:8080/examples/business/test.mov");
        videoView.load();
        relativeLayout.addView(videoView);

    }
}

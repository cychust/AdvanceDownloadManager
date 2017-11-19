package com.example.cyc.downloadproject.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cyc.downloadproject.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.statistic.WBAgent;

/**
 * Created by cyc20 on 2017/11/19.
 */

public class WBLoginActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_login);
        WbSdk.install(this,new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));
        this.findViewById(R.id.feature_oauth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLog();
                startActivity(new Intent(WBLoginActivity.this,WBAuthActivity.class));
            }
        });
    }

    public void initLog() {
        WBAgent.setAppKey(Constants.APP_KEY);
        WBAgent.setChannel("weibo"); //这个是统计这个app 是从哪一个平台down下来的  百度手机助手
        WBAgent.openActivityDurationTrack(false);
        try {
            //设置发送时间间隔 需大于90s小于8小时
            WBAgent.setUploadInterval(91000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

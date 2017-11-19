package com.example.cyc.downloadproject.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.cyc.downloadproject.R;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.text.SimpleDateFormat;


/**
 * Created by cyc20 on 2017/11/19.
 */

public class WBAuthActivity extends Activity {
    private static final String TAG ="weibosdk";
    private TextView mTokenText;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler ssoHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // 获取 Token View，并让提示 View 的内容可滚动（小屏幕可能显示不全）
        mTokenText=(TextView)findViewById(R.id.token_text_view);
        TextView hintView=(TextView)findViewById(R.id.obtain_token_hint);
        hintView.setMovementMethod(new ScrollingMovementMethod());
        //创建微博实例
        ssoHandler =new SsoHandler(WBAuthActivity.this);
        //SSO授权，仅客户端
        findViewById(R.id.obtain_token_via_sso).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ssoHandler.authorizeClientSso(new SelfWbAuthListener());
            }
        });
        //SSO授权，仅WEb
        findViewById(R.id.obtain_token_via_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ssoHandler.authorizeWeb(new SelfWbAuthListener());
            }
        });
        // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
        findViewById(R.id.obtain_token_via_signature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ssoHandler.authorize(new SelfWbAuthListener());
            }
        });
        //用户登出
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessTokenKeeper.clear(getApplicationContext());
                mAccessToken=new Oauth2AccessToken();
                updateTokenView(false);
            }
        });
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mAccessToken.getRefreshToken())){
                    AccessTokenKeeper.refreshToken(Constants.APP_KEY, WBAuthActivity.this, new RequestListener() {
                        @Override
                        public void onComplete(String response) {

                        }

                        @Override
                        public void onWeiboException(WeiboException e) {

                        }
                    });
                }
            }
        });
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler!=null){
            ssoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }

    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }
}

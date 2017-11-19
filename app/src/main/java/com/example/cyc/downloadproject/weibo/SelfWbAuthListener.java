package com.example.cyc.downloadproject.weibo;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;

/**
 * Created by cyc20 on 2017/11/19.
 */

public class SelfWbAuthListener implements WbAuthListener {
    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
        
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {

    }

}

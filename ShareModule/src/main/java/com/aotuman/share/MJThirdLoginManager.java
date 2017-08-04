package com.aotuman.share;

import android.app.Activity;
import android.content.Intent;

import com.aotuman.share.activity.QQLoginActivity;
import com.aotuman.share.activity.WBLoginActivity;
import com.aotuman.share.activity.WXLoginAndShareActivity;
import com.aotuman.share.entity.LoginChannelType;
import com.aotuman.share.listener.LoginBackListener;
import com.aotuman.share.listener.LoginListener;

/**
 * Created by aotuman on 2017/6/30.
 */

public class MJThirdLoginManager {
    private StatusManager mStatusManager;
    public LoginListener mLoginListener;
    public MJThirdLoginManager() {
        mStatusManager = new StatusManager();
    }

    public void login(Activity activity, LoginChannelType type, LoginListener listener) {
        mLoginListener = listener;
        LoginBackListener.getInstance().setLoginListener(listener);
        if (mStatusManager.loginInstalledCheck(type, activity)) {

            switch (type) {
                case QQ:
                    Intent qqIntent = new Intent(activity, QQLoginActivity.class);
                    activity.startActivity(qqIntent);
                    break;
                case WB:
                    Intent wbIntent = new Intent(activity, WBLoginActivity.class);
                    activity.startActivity(wbIntent);
                    break;
                case WX:
                    WXLoginAndShareActivity.login(activity);
                    break;
            }
        } else {
            listener.onError();
        }
    }
}

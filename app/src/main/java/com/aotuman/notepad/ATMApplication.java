package com.aotuman.notepad;

import android.app.Application;
import android.content.Context;

import com.aotuman.notepad.base.NotepadApplication;
import com.aotuman.share.entity.ShareNewConfig;

/**
 * Created by 凹凸曼 on 2016/12/7.
 */

public class ATMApplication extends Application{
    private static ATMApplication instance;
    public static ATMApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        NotepadApplication.initContext(base);
        ShareNewConfig.initShareKey(BuildConfig.SHARE_KEY_WEIXIN,BuildConfig.SHARE_KEY_WEIXIN_SECRET,BuildConfig.SHARE_KEY_SINA,BuildConfig.SHARE_KEY_QQ,BuildConfig.SHARE_SINA_OAUTH_URL,BuildConfig.SHARE_SINA_OAUTH_SCOPE);
    }
}

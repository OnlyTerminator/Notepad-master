package com.aotuman.share.entity;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class ShareNewConfig {
    public static final String sShareDir;

    static {
        sShareDir = Environment.getExternalStorageDirectory().getPath() + File.separator + "share";
    }

    public static final String ACCESS_TOKEN = "access_token";
    public static final String UID = "uid";

    private static String keyWeixin;
    private static String keyWeixinSecret;
    private static String keySina;
    private static String keyQQ;

    /**
     * TODO 新浪app_key
     */
    public static String SINA_OAUTH_REDIRECT_URL ;
    public static String SINA_OAUTH_SCOPE = "";
    public static final String HTTP_SERVER_HEAD = "https://api.weibo.com/2";
    public static final String HTTP_GET_USERINFO = "/users/show.json";
    public static final String HTTPMETHOD_GET = "GET";


    public static void initShareKey(String keyWeixin, String keyWeixinSecret, String keySina, String keyQQ,String sinaOauthUrl,String sinOauthScope) {
        ShareNewConfig.keyWeixin = keyWeixin;
        ShareNewConfig.keyWeixinSecret = keyWeixinSecret;
        ShareNewConfig.keySina = keySina;
        ShareNewConfig.keyQQ = keyQQ;
        ShareNewConfig.SINA_OAUTH_REDIRECT_URL = sinaOauthUrl;
        ShareNewConfig.SINA_OAUTH_SCOPE = sinOauthScope;
        Log.i("ShareNewConfig","keywx:"+keyWeixin);
        Log.i("ShareNewConfig","keywxs:"+keyWeixinSecret);
        Log.i("ShareNewConfig","keysina:"+keySina);
        Log.i("ShareNewConfig","keyqq:"+keyQQ);
        Log.i("ShareNewConfig","keysina_url:"+SINA_OAUTH_REDIRECT_URL);
        Log.i("ShareNewConfig","keysina_s:"+SINA_OAUTH_SCOPE);

    }


    public static String getKeyWeixin() {
        return keyWeixin;
    }

    public static String getKeyWeixinSecret() {
        return keyWeixinSecret;
    }

    public static String getKeySina() {
        return keySina;
    }

    public static String getKeyQQ() {
        return keyQQ;
    }
}

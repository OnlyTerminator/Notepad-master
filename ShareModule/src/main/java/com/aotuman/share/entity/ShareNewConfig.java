package com.aotuman.share.entity;

import android.os.Environment;
import android.text.TextUtils;

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
    public static final String SINA_OAUTH_SCOPE = "email,follow_app_official_microblog";
    public static final String HTTP_SERVER_HEAD = "https://api.weibo.com/2";
    public static final String HTTP_GET_USERINFO = "/users/show.json";
    public static final String HTTPMETHOD_GET = "GET";

    public static final String SINA_STR_FACE_URL = "profile_image_url";
    public static final String SINA_STR_NICK = "screen_name";
    public static final String SINA_STR_SEX = "gender";

    private static final String MJ_KEY_QQ = "100255986";
    private static final String MJ_KEY_SINA = "292355222";
    private static final String MJ_KEY_WEIXIN = "wx300c410f4257c6f3";
    private static final String MJ_KEY_WEIXIN_SECRET = "99a0e1a093576fcab315baa95270737f";


    public static void initShareKey(String keyWeixin, String keyWeixinSecret, String keySina, String keyQQ,String sinaOauthUrl) {
        ShareNewConfig.keyWeixin = keyWeixin;
        ShareNewConfig.keyWeixinSecret = keyWeixinSecret;
        ShareNewConfig.keySina = keySina;
        ShareNewConfig.keyQQ = keyQQ;
        ShareNewConfig.SINA_OAUTH_REDIRECT_URL = sinaOauthUrl;
    }


    public static String getKeyWeixin() {
        if (TextUtils.isEmpty(keyWeixin)) {
            return MJ_KEY_WEIXIN;
        }
        return keyWeixin;
    }

    public static String getKeyWeixinSecret() {
        if (TextUtils.isEmpty(keyWeixinSecret)) {
            return MJ_KEY_WEIXIN_SECRET;
        }
        return keyWeixinSecret;
    }

    public static String getKeySina() {
        if (TextUtils.isEmpty(keySina)) {
            return MJ_KEY_SINA;
        }
        return keySina;
    }

    public static String getKeyQQ() {
        if (TextUtils.isEmpty(keyQQ)) {
            return MJ_KEY_QQ;
        }
        return keyQQ;
    }
}

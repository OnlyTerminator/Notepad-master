package com.aotuman.share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ThirdLoginInfo;
import com.aotuman.share.listener.LoginBackListener;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.net.HttpManager;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONObject;

/**
 * Created by aotuman on 2017/6/30.
 */

public class WBLoginActivity extends Activity implements WbAuthListener {
    private static final String TAG = "WBLoginActivity";
    private SsoHandler mSsoHandler;
    private ThirdLoginInfo mThirdLoginInfo;
    private boolean mIsFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mSsoHandler == null) {
            AuthInfo authInfo = new AuthInfo(this, ShareNewConfig.getKeySina(), ShareNewConfig.SINA_OAUTH_REDIRECT_URL, ShareNewConfig.SINA_OAUTH_SCOPE);
            WbSdk.install(this,authInfo);
            mSsoHandler = new SsoHandler(this);
        }

        if (savedInstanceState == null) {
            // 防止不保留活动情况下activity被重置后直接进行操作的情况
            doLogin();
        }
    }

    /**
     * 因为微博客户端在用户取消分享后，用户点击保存到草稿箱后就不能接收到回调。
     * 因此，在这里必须进行强制关闭，不能依赖回调来关闭。
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirst) {
            mIsFirst = false;
        } else {
            // 这里处理通过网页登录无回调的问题
            finish();
        }
    }

    /**
     * 解析用户【登录】的结果
     * SSO 授权回调   重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        finish();
    }

    private void doLogin() {
        mSsoHandler.authorizeClientSso(WBLoginActivity.this);
    }

    /**
     * 加载微博用户信息
     */
    private void getThirdLoginInfo(Oauth2AccessToken accessToken) {
        mThirdLoginInfo = new ThirdLoginInfo();
        mThirdLoginInfo.access_token = accessToken.getToken();
        mThirdLoginInfo.uid = accessToken.getUid();
        new Thread(){
            @Override
            public void run() {
                try {
                    mThirdLoginInfo.thirdJson = getUserInfoByUid(mThirdLoginInfo.access_token, mThirdLoginInfo.uid);

                    LoginBackListener.getInstance().onSuccess(mThirdLoginInfo);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    LoginBackListener.getInstance().onError();
                }
            }
        }.start();

    }

    private String getUserInfoByUid(String token, String uId) throws Exception {
        String url = ShareNewConfig.HTTP_SERVER_HEAD + ShareNewConfig.HTTP_GET_USERINFO;
        WeiboParameters params = new WeiboParameters(ShareNewConfig.getKeySina());
        params.put(ShareNewConfig.UID, uId);
        params.put(ShareNewConfig.ACCESS_TOKEN, token);
        return HttpManager.openUrl(WBLoginActivity.this.getApplicationContext(), url, ShareNewConfig.HTTPMETHOD_GET, params);
    }

    /**
     * json解析 用于解析新浪微博返回的response，获取用户信息
     */
    public String getStringFromJSON(String response, String key) {
        JSONObject jsonObj;
        String returnStr = "";
        try {
            jsonObj = new JSONObject(response);
            returnStr = jsonObj.getString(key);
        } catch (Exception e) {
            Log.e("SINA", e.toString());
        }
        return returnStr;
    }

    @Override
    public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
        if (oauth2AccessToken != null) {
            if (oauth2AccessToken.isSessionValid()) {
                getThirdLoginInfo(oauth2AccessToken);
            } else {
                LoginBackListener.getInstance().onError();
            }
        }else {
            LoginBackListener.getInstance().onError();
        }
    }

    @Override
    public void cancel() {
        LoginBackListener.getInstance().onCancel();
    }

    @Override
    public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
        LoginBackListener.getInstance().onError();
    }
}

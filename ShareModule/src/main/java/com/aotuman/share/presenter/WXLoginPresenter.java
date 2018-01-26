package com.aotuman.share.presenter;

import android.content.Context;
import android.util.Log;

import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ThirdLoginInfo;
import com.aotuman.share.listener.LoginBackListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aotuman on 2017/7/14.
 */

public class WXLoginPresenter{
    private static final String TAG = "WXLoginPresenter";
    private Context mContext;
    public WXLoginPresenter(Context context){
        mContext = context;
    }
    /**
     * 返回：
     * {
     * "access_token":"ACCESS_TOKEN", // token
     * "expires_in":7200,
     * "refresh_token":"REFRESH_TOKEN",
     * "openid":"OPENID",
     * "scope":"SCOPE",
     * "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    public void handlerLoginResp(String code) {

        WeiboParameters parameters = new WeiboParameters(null);
        parameters.put("appid",ShareNewConfig.getKeyWeixin());
        parameters.put("secret",ShareNewConfig.getKeyWeixinSecret());
        parameters.put("grant_type","authorization_code");
        parameters.put("code",code);
        new AsyncWeiboRunner(mContext).requestAsync("https://api.weixin.qq.com/sns/oauth2/access_token", parameters, "GET", new RequestListener() {
            @Override
            public void onComplete(String result) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(result);
                    String token = jsonObject.getString("access_token"); // 接口调用凭证
                    String openid = jsonObject.getString("openid"); // 授权用户唯一标识
                    getThirdLoginInfo(token, openid);
                } catch (JSONException e) {
                    LoginBackListener.getInstance().onError();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                LoginBackListener.getInstance().onError();
            }
        });
    }

    private void getThirdLoginInfo(final String accessToken, final String openId) {
        WeiboParameters parameters = new WeiboParameters(null);
        parameters.put("accexx_token",accessToken);
        parameters.put("openid",openId);
        new AsyncWeiboRunner(mContext).requestAsync("https://api.weixin.qq.com/sns/userinfo", parameters, "GET", new RequestListener() {
            @Override
            public void onComplete(String result) {
                ThirdLoginInfo mThirdLoginInfo = new ThirdLoginInfo();
                mThirdLoginInfo.access_token = accessToken;
                mThirdLoginInfo.uid = openId;
                mThirdLoginInfo.thirdJson = result;
                LoginBackListener.getInstance().onSuccess(mThirdLoginInfo);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                LoginBackListener.getInstance().onError();
            }
        });
    }

    /**
     * json解析 用于解析微信返回的response，获取用户信息
     */
    private String getStringFromJSON(String response, String key) {
        JSONObject jsonObj;
        String returnStr = "";
        try {
            jsonObj = new JSONObject(response);
            returnStr = jsonObj.getString(key);
        } catch (Exception e) {
            Log.e("WEIXIN", e.toString());
        }
        return returnStr;
    }
}

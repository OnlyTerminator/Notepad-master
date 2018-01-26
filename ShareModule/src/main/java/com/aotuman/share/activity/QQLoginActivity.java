package com.aotuman.share.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ThirdLoginInfo;
import com.aotuman.share.listener.LoginBackListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by aotuman on 2017/6/30.
 */

public class QQLoginActivity extends Activity {
    private static final String TAG = "QQLoginActivity";
    private MJIUiListener mIUiListener;
    public static final String TENCENT_SCOP_ALL = "all";
    private Tencent mTencent;

    /**
     * 防止不保留活动情况下activity被重置后直接进行操作的情况
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mIUiListener = new MJIUiListener(new WeakReference<>(this));
            doLogin(this, ShareNewConfig.getKeyQQ());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mIUiListener != null) {
            Tencent.handleResultData(data, mIUiListener);
        }
        finish();
    }

    /**
     * 获取QQ授权账户信息
     */
    private void getAuthUserInfo(Context context, UserInfoIUiListener userInfoIUiListener) {
        UserInfo info = new UserInfo(context, mTencent.getQQToken());
        info.getUserInfo(userInfoIUiListener);
    }

    private ThirdLoginInfo handleResult(JSONObject object) {
        ThirdLoginInfo mThirdLoginInfo = new ThirdLoginInfo();
        mThirdLoginInfo.access_token = mTencent.getAccessToken();
        mThirdLoginInfo.uid = mTencent.getOpenId();
        mThirdLoginInfo.thirdJson = object.toString();
        return mThirdLoginInfo;
    }

    private void doLogin(Activity activity, String appId) {
        mTencent = Tencent.createInstance(appId, activity);
        if (null != mTencent) {
            mTencent.login(activity, TENCENT_SCOP_ALL, mIUiListener);
        }
    }

    /**
     * 获取用户信息监听器
     */
    private class UserInfoIUiListener implements IUiListener {

        @Override
        public void onCancel() {
            LoginBackListener.getInstance().onCancel();
        }

        @Override
        public void onComplete(Object obj) {
            try {
                if (obj != null && obj instanceof JSONObject) {
                    JSONObject object = (JSONObject) obj;
                    ThirdLoginInfo info = handleResult(object);
                    LoginBackListener.getInstance().onSuccess(info);
                } else {
                    LoginBackListener.getInstance().onError();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                LoginBackListener.getInstance().onError();
            }
        }

        @Override
        public void onError(UiError uiError) {
            LoginBackListener.getInstance().onError();
        }

    }

    private static class MJIUiListener implements IUiListener {
        private WeakReference<QQLoginActivity> mWR;

        public MJIUiListener(WeakReference<QQLoginActivity> wr) {
            mWR = wr;
        }

        @Override
        public void onComplete(Object object) {
            QQLoginActivity activity = mWR.get();
            if (null != activity && !activity.isFinishing()) {
                JSONObject jsonObject = ((JSONObject) object);
                try {
                    String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
                    String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
                    String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
                    activity.mTencent.setAccessToken(token, expires);
                    activity.mTencent.setOpenId(openId);
                    activity.getAuthUserInfo(activity, activity.new UserInfoIUiListener());
                } catch (Exception e) {
                    LoginBackListener.getInstance().onError();
                }
            } else {
                LoginBackListener.getInstance().onError();
            }
        }

        @Override
        public void onError(UiError uiError) {
            LoginBackListener.getInstance().onError();
        }

        @Override
        public void onCancel() {
            LoginBackListener.getInstance().onCancel();
        }
    }

}

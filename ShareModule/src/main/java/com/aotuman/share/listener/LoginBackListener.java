package com.aotuman.share.listener;

import com.aotuman.share.entity.ThirdLoginInfo;

/**
 * Created by xufeng.zhang on 2017/8/2.
 */

public class LoginBackListener implements LoginListener {
    private LoginListener mListener;
    private static LoginBackListener mInstance;
    private LoginBackListener() {
    }

    public static LoginBackListener getInstance() {
        if (mInstance == null) {
            mInstance = new LoginBackListener();
        }
        return mInstance;
    }

    public void setLoginListener(LoginListener shareListener) {
        mListener = shareListener;
    }

    @Override
    public void onSuccess(ThirdLoginInfo thirdLoginInfo) {
        if(null != mListener){
            mListener.onSuccess(thirdLoginInfo);
            mListener = null;
        }
    }

    @Override
    public void onError() {
        if(null != mListener){
            mListener.onError();
            mListener = null;
        }
    }

    @Override
    public void onCancel() {
        if(null != mListener){
            mListener.onCancel();
            mListener = null;
        }
    }
}

package com.aotuman.share.listener;

import com.aotuman.share.entity.ShareChannelType;

/**
 * Created by xufeng.zhang on 2017/8/2.
 */

public class ShareBackListener implements ShareListener {
    private ShareListener mListener;
    private static ShareBackListener mInstance;
    private ShareChannelType mChannelType;
    private ShareBackListener() {
    }

    public static ShareBackListener getInstance() {
        if (mInstance == null) {
            mInstance = new ShareBackListener();
        }
        return mInstance;
    }

    public void setShareListener(ShareListener shareListener) {
        mListener = shareListener;
    }

    public void setShareChannelType(ShareChannelType channelType){
        mChannelType = channelType;
    }
    @Override
    public void onSuccess(ShareChannelType type) {
        if (null != mListener) {
            mListener.onSuccess(mChannelType);
            mListener = null;
        }
    }

    @Override
    public void onError(ShareChannelType type) {
        if (null != mListener) {
            mListener.onError(mChannelType);
            mListener = null;
        }
    }

    @Override
    public void onCancel(ShareChannelType type) {
        if (null != mListener) {
            mListener.onCancel(mChannelType);
            mListener = null;
        }
    }
}

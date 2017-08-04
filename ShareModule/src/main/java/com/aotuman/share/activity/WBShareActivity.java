package com.aotuman.share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ShareRealContent;
import com.aotuman.share.listener.ShareBackListener;
import com.aotuman.share.presenter.WBSharePresenter;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by aotuman on 2017/6/30.
 */

public class WBShareActivity extends Activity implements WbShareCallback {
    private WbShareHandler mWbShareHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            AuthInfo authInfo = new AuthInfo(this, ShareNewConfig.getKeySina(), ShareNewConfig.SINA_OAUTH_REDIRECT_URL, ShareNewConfig.SINA_OAUTH_SCOPE);
            WbSdk.install(this,authInfo);
            // 防止不保留活动情况下activity被重置后直接进行操作的情况
            doShare();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWbShareHandler.doResultIntent(intent,this);
    }

    private void doShare() {
        mWbShareHandler = new WbShareHandler(this);
        mWbShareHandler.registerApp();
        // 建立请求体
        final ShareRealContent content = (ShareRealContent) getIntent().getSerializableExtra("shareContent");
        if (content == null) {
            throw new NullPointerException("ShareContent is null，intent = " + getIntent());
        }
        new Thread(){
            @Override
            public void run() {
                WeiboMultiMessage request = new WBSharePresenter(WBShareActivity.this).createShareObject(content);
                if(null != request) {
                    mWbShareHandler.shareMessage(request, false);
                }else {
                    ShareBackListener.getInstance().onError(ShareChannelType.WB);
                    WBShareActivity.this.finish();
                }
            }
        }.start();
    }

    @Override
    public void onWbShareSuccess() {
        ShareBackListener.getInstance().onSuccess(ShareChannelType.WB);
        finish();
    }

    @Override
    public void onWbShareCancel() {
        ShareBackListener.getInstance().onCancel(ShareChannelType.WB);
        finish();
    }

    @Override
    public void onWbShareFail() {
        ShareBackListener.getInstance().onError(ShareChannelType.WB);
        finish();
    }

}

package com.aotuman.share.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ShareRealContent;
import com.aotuman.share.listener.ShareBackListener;
import com.aotuman.share.presenter.QQSharePresenter;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by aotuman on 2017/6/30.
 */

public class QQShareActivity extends Activity {
    private MJIUListener mIUiListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (savedInstanceState == null) {
            ShareRealContent shareContent = (ShareRealContent) intent.getSerializableExtra("shareContent");
            mIUiListener = new MJIUListener();
            doShare(shareContent, ShareNewConfig.getKeyQQ());
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

    private void doShare(final ShareRealContent shareContent, final String appId) {
        new Thread(){
            @Override
            public void run() {
                Tencent tencent = Tencent.createInstance(appId, QQShareActivity.this.getApplicationContext());
                Bundle params = new QQSharePresenter(QQShareActivity.this).createShareObject(shareContent);
                if (null != params) {
                    tencent.shareToQQ(QQShareActivity.this, params, mIUiListener);
                } else {
                    ShareBackListener.getInstance().onError(ShareChannelType.QQ);
                    finish();
                }
            }
        }.start();
    }

    private static class MJIUListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            ShareBackListener.getInstance().onSuccess(ShareChannelType.QQ);
        }

        @Override
        public void onError(UiError uiError) {
            ShareBackListener.getInstance().onError(ShareChannelType.QQ);
        }

        @Override
        public void onCancel() {
            ShareBackListener.getInstance().onCancel(ShareChannelType.QQ);
        }

    }

}

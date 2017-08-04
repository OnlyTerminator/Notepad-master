package com.aotuman.share.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareNewConfig;
import com.aotuman.share.entity.ShareRealContent;
import com.aotuman.share.listener.LoginBackListener;
import com.aotuman.share.listener.ShareBackListener;
import com.aotuman.share.presenter.WXLoginPresenter;
import com.aotuman.share.presenter.WXSharePresenter;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by aotuman on 2017/6/30.
 */

public class WXLoginAndShareActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = WXAPIFactory.createWXAPI(this, ShareNewConfig.getKeyWeixin(), true);
        mApi.handleIntent(getIntent(), this);
        finish();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mApi != null) {
            mApi.handleIntent(getIntent(), this);
        }
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp != null) {
            if (baseResp instanceof SendAuth.Resp) {
                parseLoginResp((SendAuth.Resp) baseResp);
            } else {
                parseShareResp(baseResp);
            }
        } else {
            ShareBackListener.getInstance().onError(null);
        }
        finish();
    }

    public static void login(Context context) {
        String appId = ShareNewConfig.getKeyWeixin();
        if (TextUtils.isEmpty(appId)) {
            throw new NullPointerException("请通过shareBlock初始化WeiXinAppId");
        }

        IWXAPI api = WXAPIFactory.createWXAPI(context, appId, true);
        api.registerApp(appId);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        api.sendReq(req); // 这里的请求的回调会在activity中收到，然后通过parseLoginResp方法解析
    }

    /**
     * 解析用户登录的结果
     */
    protected void parseLoginResp(SendAuth.Resp resp) {
        // 有可能是listener传入的是null，也可能是调用静态方法前没初始化当前的类
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK: // 登录成功
                new WXLoginPresenter(WXLoginAndShareActivity.this).handlerLoginResp(resp.code); // 登录成功后开始通过code换取token
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                LoginBackListener.getInstance().onCancel();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                LoginBackListener.getInstance().onError();
                break;
            default:
                LoginBackListener.getInstance().onError();
                break;
        }
    }


//********************************************************分享逻辑*******************************************************************************


    public void doShare(final Context context, final ShareRealContent shareContent, final ShareChannelType shareType) {
        new Thread(){
            @Override
            public void run() {
                String weChatAppId = ShareNewConfig.getKeyWeixin();
                if (TextUtils.isEmpty(weChatAppId)) {
                    throw new NullPointerException("请通过shareBlock初始化WeChatAppId");
                }
                IWXAPI api = WXAPIFactory.createWXAPI(context, weChatAppId, true);
                api.registerApp(weChatAppId);
                api.sendReq(new WXSharePresenter(WXLoginAndShareActivity.this).createShareRequest(shareContent, shareType)); // factory
            }
        }.start();
    }
    /**
     * 解析分享到微信的结果
     * <p>
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419318634&token=&lang=zh_CN
     */
    private void parseShareResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ShareBackListener.getInstance().onSuccess(null);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ShareBackListener.getInstance().onCancel(null);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ShareBackListener.getInstance().onError(null);
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                ShareBackListener.getInstance().onError(null);
                break;
            case BaseResp.ErrCode.ERR_COMM:
                ShareBackListener.getInstance().onError(null);
                break;
            default:
                ShareBackListener.getInstance().onError(null);
        }
    }
}

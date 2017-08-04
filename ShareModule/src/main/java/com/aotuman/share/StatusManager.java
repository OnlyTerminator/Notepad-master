package com.aotuman.share;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aotuman.share.entity.LoginChannelType;
import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareNewConfig;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.auth.WbAppInfo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.lang.reflect.Field;

/**
 * Created by aotuman on 2017/7/03.
 */
public class StatusManager {
    private static final String TAG = "StatusManger";
    public boolean loginInstalledCheck(LoginChannelType type, Activity activity) {
        switch (type) {
            case QQ:
                return isQQInstalled(activity);
            case WX:
                return isWeiXinInstalled(activity);
            case WB:
                return isWeiBoInstalled(activity);
            default:
                return false;
        }

    }

    public boolean shareInstalledCheck(ShareChannelType type, Activity activity) {
        switch (type) {
            case QQ:
                return isQQInstalled(activity);
            case WX_FRIEND:
            case WX_TIMELINE:
                return isWeiXinInstalled(activity);
            case WB:
                return isWeiBoInstalled(activity);
            case MESSAGE:
                return true;
            default:
                return false;
        }

    }

    private boolean isQQInstalled(Activity activity) {
        Tencent tencent = Tencent.createInstance(ShareNewConfig.getKeyQQ(), activity);
        return null != tencent && tencent.isSupportSSOLogin(activity);
    }

    private boolean isWeiBoInstalled(Context context) {
        try {
            Field field = null;
            field = WeiboAppManager.class.getDeclaredField("wbAppInfo");
            field.setAccessible(true);
            field.set(WeiboAppManager.getInstance(context), null);//重置为null，清除缓存
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        } catch (NoSuchFieldException e) {
            Log.e(TAG, e.toString());
        }
        WbAppInfo wbAppInfo = WeiboAppManager.getInstance(context).getWbAppInfo();
        return wbAppInfo != null && wbAppInfo.isLegal();

    }

    private boolean isWeiXinInstalled(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, ShareNewConfig.getKeyWeixin(), true);
        return api.isWXAppInstalled();
    }

}

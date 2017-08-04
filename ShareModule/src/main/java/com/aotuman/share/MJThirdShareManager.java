package com.aotuman.share;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.aotuman.share.activity.QQShareActivity;
import com.aotuman.share.activity.SMSShareControl;
import com.aotuman.share.activity.WBShareActivity;
import com.aotuman.share.activity.WXLoginAndShareActivity;
import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareContentConfig;
import com.aotuman.share.entity.ShareRealContent;
import com.aotuman.share.listener.DataPrepareListener;
import com.aotuman.share.listener.ShareBackListener;
import com.aotuman.share.listener.ShareListener;
import com.aotuman.share.view.SharePlatform;

/**
 * Created by aotuman on 2017/6/30.
 */

public class MJThirdShareManager implements DataPrepareListener {
    private static final String TAG = "MJThirdShareManager";
    private ShareListener mShareListener;
    private Activity mContext;
    private ShareContentConfig mShareContent;
    //===========以弹出底部PopupWindow的方式进行分享========================================
    private SharePlatform mPlatformDialog;
    //    private MJDialog mLoadingDialog;
    private StatusManager mStatusManager;
    private boolean mDataPrepare;
    private ShareChannelType mChannelType;

    public MJThirdShareManager(Activity activity, ShareListener listener) {
        mShareListener = listener;
        ShareBackListener.getInstance().setShareListener(listener);
        mContext = activity;
        mStatusManager = new StatusManager();
    }

    /**
     * 分享方法
     *
     * @param shareContent 分享内容
     * @param needLoading  是否需要点击之后的loading依据个人情况设置
     */
    public void doShare(ShareContentConfig shareContent, boolean needLoading) {
        if (!needLoading) {
            mDataPrepare = true;
        }
        mChannelType = null;
        mShareContent = shareContent;
        showPlatformDialog();
    }

    /**
     * @param type         直接传入要分享的渠道，直接分享，不展示Dialog
     * @param shareContent 分享内容
     * @param needLoading  是否需要点击之后的loading依据个人情况设置
     */
    public void doShare(ShareChannelType type, ShareContentConfig shareContent, boolean needLoading) {
        if (!needLoading) {
            mDataPrepare = true;
        }
        mChannelType = type;
        mShareContent = shareContent;
        if (mDataPrepare) {
            prepareShare(mContext, type, mShareContent.getRealContent(type), mShareListener);
        }
    }

    /**
     * 用来展示分享popUpWindow， 以弹出底部PopupWindow的方式进行分享
     */
    private void showPlatformDialog() {
//        dismissDialog();
        boolean canShowDialog = mContext != null && !mContext.isFinishing();
        if (Build.VERSION.SDK_INT > 17) {
            if (null == mContext || mContext.isDestroyed()) {
                canShowDialog = false;
            }
        }
        if (!canShowDialog) {
            if (null != mShareListener) {
                mShareListener.onError(null);
            }
            return;
        }
        mPlatformDialog = new SharePlatform(mContext, mShareContent.mShareType, new SharePlatform.IShareClickCallback() {
            @Override
            public void onShareCallback(ShareChannelType type) {
                mChannelType = type;
                if (mDataPrepare) {
                    prepareShare(mContext, type, mShareContent.getRealContent(type), mShareListener);
//                    dismissDialog();
                } else {
//                    showLoadingDialog();
                }
            }

        });
        mPlatformDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mPlatformDialog != null) {
                    mPlatformDialog.releaseResource();
                    mPlatformDialog = null;
                }
            }
        });
        mPlatformDialog.show();
    }

    public boolean isDialogShow() {
        return mPlatformDialog != null && mPlatformDialog.isShowing();
    }

//    private void showLoadingDialog() {
//        dismissDialog();
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceTool.dp2px(120), DeviceTool.dp2px(120));
//        mLoadingDialog = new MJDialogLoadingControl.Builder(mContext).loadingMsg(mContext.getString(R.string.capture_screen)).layoutParams(params).build();
//        mLoadingDialog.show();
//    }

//    private void dismissDialog() {
//        if (null != mPlatformDialog && mPlatformDialog.isShowing()) {
//            mPlatformDialog.dismiss();
//        }
//        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//    }

    /**
     * 准备图片，贴上模糊背景图
     *
     * @param activity
     * @param channelType
     * @param content
     * @param listener
     */
    private void prepareShare(final Activity activity, final ShareChannelType channelType, final ShareRealContent content, final ShareListener listener) {
        doRealShare(activity, channelType, content, listener);
    }

    /**
     * 分享
     *
     * @param activity
     * @param channelType
     * @param content
     * @param listener
     */
    private void doRealShare(Activity activity, ShareChannelType channelType, ShareRealContent content, ShareListener listener) {
        mChannelType = channelType;
        ShareBackListener.getInstance().setShareChannelType(mChannelType);
        if (mStatusManager.shareInstalledCheck(channelType, activity)) {
            switch (channelType) {
                case QQ:
                    Intent qqIntent = new Intent(activity, QQShareActivity.class);
                    qqIntent.putExtra("shareContent", content);
                    activity.startActivity(qqIntent);
                    break;
                case WB:
                    Intent wbIntent = new Intent(activity, WBShareActivity.class);
                    wbIntent.putExtra("shareContent", content);
                    activity.startActivity(wbIntent);
                    break;
                case WX_FRIEND:
                    new WXLoginAndShareActivity().doShare(activity, content, ShareChannelType.WX_FRIEND);
                    break;
                case WX_TIMELINE:
                    new WXLoginAndShareActivity().doShare(activity, content, ShareChannelType.WX_TIMELINE);
                    break;
                case MESSAGE:
                    new SMSShareControl().doShare(activity, content);
                    break;
            }
        } else {
            Toast.makeText(activity, "未安装要分享的APP", Toast.LENGTH_SHORT).show();
            if (null != listener) {
                listener.onError(channelType);
            }
        }
    }

    @Override
    public void prepareSuccess(boolean flag) {
        mDataPrepare = flag;
        if (null != mChannelType) {
            if (mDataPrepare) {
                prepareShare(mContext, mChannelType, mShareContent.getRealContent(mChannelType), mShareListener);
//                dismissDialog();
            } else {
                Toast.makeText(mContext, "分享数据获取失败", Toast.LENGTH_LONG).show();
//                dismissDialog();
            }
        }
    }
}

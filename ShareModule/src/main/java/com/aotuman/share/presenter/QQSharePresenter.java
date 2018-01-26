package com.aotuman.share.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.aotuman.share.CommonUtils;
import com.aotuman.share.entity.ShareRealContent;
import com.tencent.connect.share.QQShare;

import java.io.File;

/**
 * Created by aotuman on 2017/7/14.
 */

public class QQSharePresenter {
    private Context mContext;
    public QQSharePresenter(Context context){
        mContext = context;
    }
    public Bundle createShareObject(ShareRealContent shareContent) {
        Bundle bundle;
        switch (shareContent.mShareContentType) {
            case TEXT:
                // 纯文字
                // 文档中说： "本接口支持3种模式，每种模式的参数设置不同"，这三种模式中不包含纯文本
                Toast.makeText(mContext, "目前不支持分享纯文本信息给QQ好友", Toast.LENGTH_SHORT).show();// fake bundle
                return null;
            case PIC:
                // 纯图片
                bundle = getImageObj(shareContent);
                break;
            case WEBPAGE:
                // 网页
                bundle = getWebPageObj(shareContent);
                break;
            case MUSIC:
                bundle = getMusicObj(shareContent);
                break;
            case APP:
                bundle = getAPPObj(shareContent);
                break;
            default:
                throw new UnsupportedOperationException("不支持的分享内容");
        }
        return getQQFriendParams(bundle, shareContent);
    }

    private Bundle getImageObj(ShareRealContent shareContent) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE); // 标识分享的是纯图片 (必填)
        if (!TextUtils.isEmpty(shareContent.mShareLocalImage)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, shareContent.mShareLocalImage); // local uri
        }else if(null != shareContent.mShareBitmap){
            String path = CommonUtils.getFilesDir(mContext, "share").getAbsolutePath() + File.separator + "ATMShareImage.png";
            CommonUtils.writeBitmap(new File(path),shareContent.mShareBitmap,100,true);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path); // local uri
        }else {
            throw new UnsupportedOperationException("没有可以分享的图片地址");
        }
        return params;
    }

    private Bundle getWebPageObj(ShareRealContent shareContent) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        if (!TextUtils.isEmpty(shareContent.mShareURL)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareContent.mShareURL);
        }
        return params;
    }

    private Bundle getMusicObj(ShareRealContent shareRealContent){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, shareRealContent.mShareMusicUrl);
        return params;
    }

    private Bundle getAPPObj(ShareRealContent shareRealContent){
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        return params;
    }
    /**
     * @see "http://wiki.open.qq.com/wiki/mobile/API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E#1.13_.E5.88.86.E4.BA.AB.E6.B6.88.E6.81.AF.E5.88.B0QQ.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89"
     * QQShare.PARAM_TITLE 	        必填 	String 	分享的标题, 最长30个字符。
     * QQShare.SHARE_TO_QQ_KEY_TYPE 	必填 	Int 	分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
     * QQShare.PARAM_TARGET_URL 	必填 	String 	这条分享消息被好友点击后的跳转URL。
     * QQShare.PARAM_SUMMARY 	        可选 	String 	分享的消息摘要，最长40个字。
     * QQShare.SHARE_TO_QQ_IMAGE_URL 	可选 	String 	分享图片的URL或者本地路径
     * QQShare.SHARE_TO_QQ_APP_NAME 	可选 	String 	手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     * QQShare.SHARE_TO_QQ_EXT_INT 	可选 	Int 	分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：
     * QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。
     * QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
     * <p>
     * target必须是真实的可跳转链接才能跳到QQ = =！
     * <p>
     * 发送给QQ好友
     */
    private Bundle getQQFriendParams(Bundle params, ShareRealContent shareContent) {
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.mShareTitle); // 标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareContent.mShareSummary); // 描述
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareContent.mShareURL); // 这条分享消息被好友点击后的跳转URL
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "返回APP"); // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替 (可选)
        return params;
    }
}

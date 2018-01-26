package com.aotuman.share.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.aotuman.share.ThumbBitmapManager;
import com.aotuman.share.entity.ShareRealContent;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by aotuman on 2017/7/14.
 */

public class WBSharePresenter {
    private static final String TAG = "WBSharePresenter";
    private Context mContext;

    public WBSharePresenter(Context context) {
        mContext = context;
    }

    public WeiboMultiMessage createShareObject(ShareRealContent shareContent) {
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        switch (shareContent.mShareContentType) {
            case TEXT:
                // 纯文字
                weiboMultiMessage.textObject = getTextObj(shareContent);
                break;
            case PIC:
                // 纯图片
                weiboMultiMessage.imageObject = getImageObj(shareContent);
                break;
            case PICANDTEXT:
                weiboMultiMessage.imageObject = getImageObj(shareContent);
                weiboMultiMessage.textObject = getTextObj(shareContent);
                break;
            case WEBPAGE:
                // 网页
                if (!TextUtils.isEmpty(shareContent.mShareURL)) {
                    weiboMultiMessage.mediaObject = getWebPageObj(shareContent);
                }
                break;
            default:
                throw new UnsupportedOperationException("createShareObject: not support this type");
        }
        if (!weiboMultiMessage.checkArgs()) {
            return null;
        }
        return weiboMultiMessage;
    }

    /**
     * 创建文本消息对象
     */
    private TextObject getTextObj(ShareRealContent shareContent) {
        TextObject textObject = new TextObject();
        textObject.text = shareContent.mShareSummary + shareContent.mShareURL;
        return textObject;
    }

    /**
     * 创建图片消息对象
     */
    private ImageObject getImageObj(ShareRealContent shareContent) {
        ImageObject imageObject = new ImageObject();
        Bitmap b = shareContent.getThumbBitmap(mContext);
        if (null != b) {
            imageObject.setThumbImage(b);
        }
        if(null != shareContent.mShareBitmap) {
            imageObject.setImageObject(shareContent.mShareBitmap);
        }else {
            if (!TextUtils.isEmpty(shareContent.mShareLocalImage)) {
                imageObject.imagePath = shareContent.mShareLocalImage;
            }
        }
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象
     */
    private WebpageObject getWebPageObj(ShareRealContent shareContent) {
        WebpageObject mediaObject = new WebpageObject();
        buildMediaObj(mediaObject, shareContent);

        mediaObject.defaultText = shareContent.mShareSummary;
        mediaObject.actionUrl = shareContent.mShareURL;
        return mediaObject;
    }

    private void buildMediaObj(BaseMediaObject mediaObject, ShareRealContent shareContent) {
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareContent.mShareTitle;
        mediaObject.description = shareContent.mShareSummary;
        if (null != shareContent.getThumbBitmap(mContext)) {
            mediaObject.thumbData = new ThumbBitmapManager().getThumbBytes(shareContent.getThumbBitmap(mContext));
        }
    }

}

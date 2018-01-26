package com.aotuman.share.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.aotuman.share.ThumbBitmapManager;
import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareRealContent;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by aotuman on 2017/7/14.
 */

public class WXSharePresenter{
    private ThumbBitmapManager mThumbManager;
    private Context mContext;
    public WXSharePresenter(Context context) {
        mThumbManager = new ThumbBitmapManager();
        mContext = context;
    }

    public SendMessageToWX.Req createShareRequest(ShareRealContent shareContent, ShareChannelType shareType) {
        // 建立信息体
        WXMediaMessage msg = new WXMediaMessage();
        // 发送信息
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        switch (shareType) {
            case WX_FRIEND:
                msg.mediaObject = createShareObject(msg, shareContent, ShareChannelType.WX_FRIEND);
                msg.title = shareContent.mShareTitle;
                msg.description = shareContent.mShareSummary;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                break;
            case WX_TIMELINE:
                msg.mediaObject = createShareObject(msg, shareContent, ShareChannelType.WX_TIMELINE);
                msg.title = shareContent.mShareTitle;
                msg.description = shareContent.mShareSummary;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                break;
        }
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        return req;
    }

    private WXMediaMessage.IMediaObject createShareObject(WXMediaMessage msg, ShareRealContent shareContent, ShareChannelType type) {
        WXMediaMessage.IMediaObject mediaObject;
        switch (shareContent.mShareContentType) {
            case TEXT:
                // 纯文字
                mediaObject = getTextObj(shareContent, type);
                break;
            case PIC:
                // 纯图片
                mediaObject = getImageObj(shareContent);
                Bitmap shareBmp = mThumbManager.getBigBitmap(shareContent);
                if (null != shareBmp) {
                    msg.thumbData = mThumbManager.getThumbBytes(shareBmp);// 这里没有做缩略图的配置，缩略图和原图是同一个对象
                }
                break;
            case WEBPAGE:
                // 网页
                mediaObject = getWebPageObj(shareContent);
                Bitmap bitmap = shareContent.getThumbBitmap(mContext);
                if (null != bitmap) {
                    msg.thumbData = mThumbManager.getThumbBytes(bitmap);  // 设置缩略图
                }
                break;
            case MUSIC:
                mediaObject = getMusicObj(shareContent);
                Bitmap m_bitmap = shareContent.getThumbBitmap(mContext);
                if (null != m_bitmap) {
                    msg.thumbData = mThumbManager.getThumbBytes(m_bitmap);  // 设置缩略图
                }
                break;
            case VIDEO:
                mediaObject = getVideoObj(shareContent);
                Bitmap v_bitmap = shareContent.getThumbBitmap(mContext);
                if (null != v_bitmap) {
                    msg.thumbData = mThumbManager.getThumbBytes(v_bitmap);  // 设置缩略图
                }
                break;
            case PICANDTEXT:
                throw new UnsupportedOperationException("不支持的分享内容");
            default:
                throw new UnsupportedOperationException("不支持的分享内容");
        }
        if (!mediaObject.checkArgs()) {
            throw new IllegalArgumentException("分享信息的参数类型不正确");
        }
        return mediaObject;
    }

    private WXMediaMessage.IMediaObject getTextObj(ShareRealContent shareContent, ShareChannelType type) {
        WXTextObject text = new WXTextObject();
        text.text = shareContent.mShareSummary;
        return text;
    }

    private WXMediaMessage.IMediaObject getImageObj(ShareRealContent shareContent) {
        WXImageObject image = new WXImageObject();
        if(null == shareContent.mShareBitmap) {
            image.imagePath = shareContent.mShareLocalImage;
        }else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            shareContent.mShareBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            image.imageData = byteArrayOutputStream.toByteArray();
        }
        return image;
    }

    private WXMediaMessage.IMediaObject getWebPageObj(ShareRealContent shareContent) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareContent.mShareURL;
        return webPage;
    }

    private WXMediaMessage.IMediaObject getVideoObj(ShareRealContent shareContent) {
        WXVideoObject video = new WXVideoObject();
        if(!TextUtils.isEmpty(shareContent.mShareVideoUrl)) {
            video.videoUrl = shareContent.mShareVideoUrl;
        }
        return video;
    }

    private WXMediaMessage.IMediaObject getMusicObj(ShareRealContent shareContent) {
        WXMusicObject music = new WXMusicObject();
        if(!TextUtils.isEmpty(shareContent.mShareMusicUrl)) {
            music.musicUrl = shareContent.mShareMusicUrl;
        }
        return music;
    }
}

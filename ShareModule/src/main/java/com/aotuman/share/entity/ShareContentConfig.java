package com.aotuman.share.entity;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Created by aotuman on 6/30/17.
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class ShareContentConfig implements Serializable {
    private ShareRealContent qq;
    private ShareRealContent wb;
    private ShareRealContent wxf;
    private ShareRealContent wxc;
    private ShareRealContent sms;
    public LinkedHashMap<ShareChannelType, ShareContentType> mShareType = new LinkedHashMap<>();
    private ShareContentConfig(LinkedHashMap<ShareChannelType, ShareContentType> shareContentTypeArrayMap, ShareRealContent... shareContent) {
        wxf = shareContent[0];
        wxc = shareContent[1];
        qq = shareContent[2];
        wb = shareContent[3];
        sms = shareContent[4];
        setShareContentType(shareContentTypeArrayMap);
        mShareType = shareContentTypeArrayMap;
    }

    private void setShareContentType(LinkedHashMap<ShareChannelType, ShareContentType> shareContentTypeArrayMap) {
        if (null != shareContentTypeArrayMap) {
            wxf.mShareContentType = shareContentTypeArrayMap.get(ShareChannelType.WX_FRIEND);
            wxc.mShareContentType = shareContentTypeArrayMap.get(ShareChannelType.WX_TIMELINE);
            qq.mShareContentType = shareContentTypeArrayMap.get(ShareChannelType.QQ);
            wb.mShareContentType = shareContentTypeArrayMap.get(ShareChannelType.WB);
            sms.mShareContentType = shareContentTypeArrayMap.get(ShareChannelType.MESSAGE);
        }
    }

    public ShareRealContent getRealContent(ShareChannelType type) {
        switch (type) {
            case QQ:
                return qq;
            case WX_FRIEND:
                return wxf;
            case WX_TIMELINE:
                return wxc;
            case WB:
                return wb;
            case MESSAGE:
                return sms;

        }
        return null;
    }

    public static class Builder {

        private ShareRealContent qqContent;
        private ShareRealContent wbContent;
        private ShareRealContent wxFContent;
        private ShareRealContent wxCContent;
        private ShareRealContent smsContent;
        public LinkedHashMap<ShareChannelType, ShareContentType> mShareType = new LinkedHashMap<>();

        public Builder(String title, String content) {
            qqContent = new ShareRealContent(title,content);
            wbContent = new ShareRealContent(title,content);
            wxCContent = new ShareRealContent(title,content);
            wxFContent = new ShareRealContent(title,content);
            smsContent = new ShareRealContent(title,content);
            putDefaultShareType();
        }

        public Builder shareTitle(String title) {
            qqContent.mShareTitle = title;
            wbContent.mShareTitle = title;
            wxCContent.mShareTitle = title;
            wxFContent.mShareTitle = title;
            smsContent.mShareTitle = title;
            return this;
        }

        public Builder shareContent(String content) {
            qqContent.mShareSummary = content;
            wbContent.mShareSummary = content;
            wxFContent.mShareSummary = content;
            wxCContent.mShareSummary = content;
            smsContent.mShareSummary = content;
            return this;
        }

        public Builder shareUrl(String url) {
            qqContent.mShareURL = url;
            wbContent.mShareURL = url;
            wxCContent.mShareURL = url;
            wxFContent.mShareURL = url;
            smsContent.mShareURL = url;
            return this;
        }

        public Builder localImagePath(String imagePath) {
            qqContent.mShareLocalImage = imagePath;
            wbContent.mShareLocalImage = imagePath;
            wxFContent.mShareLocalImage = imagePath;
            wxCContent.mShareLocalImage = imagePath;
            smsContent.mShareLocalImage = imagePath;
            return this;
        }

        public Builder netImagePath(String imagePath) {
            qqContent.mShareNetImage = imagePath;
            wbContent.mShareNetImage = imagePath;
            wxCContent.mShareNetImage = imagePath;
            wxFContent.mShareNetImage = imagePath;
            smsContent.mShareNetImage = imagePath;
            return this;
        }

        public Builder thumbImagePath(String imagePath) {
            qqContent.mThumbPath = imagePath;
            wbContent.mThumbPath = imagePath;
            wxCContent.mThumbPath = imagePath;
            wxFContent.mThumbPath = imagePath;
            smsContent.mThumbPath = imagePath;
            return this;
        }

        public Builder needAddQRCode(boolean need) {
            qqContent.mNeedAddQRCode = need;
            wbContent.mNeedAddQRCode = need;
            wxCContent.mNeedAddQRCode = need;
            wxFContent.mNeedAddQRCode = need;
            smsContent.mNeedAddQRCode = need;
            return this;
        }

        public Builder wxFriendTitle(String title) {
            wxFContent.mShareTitle = title;
            return this;
        }

        public Builder wxFriendContent(String content) {
            wxFContent.mShareSummary = content;
            return this;
        }

        public Builder wxCircleTitle(String title) {
            wxCContent.mShareTitle = title;
            return this;
        }

        public Builder wxCircleContent(String content) {
            wxCContent.mShareSummary = content;
            return this;
        }

        public Builder qqFriendTitle(String title) {
            qqContent.mShareTitle = title;
            return this;
        }

        public Builder qqFriendContent(String content) {
            qqContent.mShareSummary = content;
            return this;
        }

        public Builder wbFriendTitle(String title) {
            wbContent.mShareTitle = title;
            return this;
        }

        public Builder wbFriendContent(String content) {
            wbContent.mShareSummary = content;
            return this;
        }

        public Builder messageContent(String content) {
            smsContent.mShareSummary = content;
            return this;
        }

        public Builder putShareType(ShareChannelType channel, ShareContentType content) {
            mShareType.put(channel, content);
            return this;
        }

        public Builder removeShareType(ShareChannelType channel) {
            mShareType.remove(channel);
            return this;
        }

        private void putDefaultShareType() {
            mShareType.put(ShareChannelType.WX_FRIEND, ShareContentType.WEBPAGE);
            mShareType.put(ShareChannelType.WX_TIMELINE, ShareContentType.PIC);
            mShareType.put(ShareChannelType.QQ, ShareContentType.PIC);
            mShareType.put(ShareChannelType.WB, ShareContentType.PICANDTEXT);
            mShareType.put(ShareChannelType.MESSAGE, ShareContentType.TEXT);
        }

        public ShareContentConfig build() {
            return new ShareContentConfig(mShareType, wxFContent, wxCContent, qqContent, wbContent, smsContent);
        }
    }
}

package com.aotuman.share.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by aotuman on 6/30/17.
 */

public class ShareRealContent implements Serializable {
    /**
     * 标题和概要是必传参数。
     */
    public String mShareTitle;

    public String mShareSummary;
    /**
     * 选传参数
     */
    public String mShareURL;

    /**
     * 由于第三方只支持分享本地图片，所以我们也是，如果需要分享网络图片，请先自行下载。
     */
    public String mShareLocalImage;

    /**
     * 需要分享的bitmap,如果有，会优先使用这个不会在根据mShareLocalImage自行生成bitmap
     */
    public Bitmap mShareBitmap;

    public ShareContentType mShareContentType;

    public String mShareVideoUrl;

    public String mShareMusicUrl;

    public ShareRealContent(String title, String content) {
        mShareTitle = title;
        mShareSummary = content;
    }

    public Bitmap getThumbBitmap(Context context) {
        Bitmap bitmap = null;
        try {
            if (null == mShareBitmap) {
                if (!TextUtils.isEmpty(mShareLocalImage)) {
                    if (mShareLocalImage.startsWith("/")) {
                        mShareLocalImage = "file://" + mShareLocalImage;
                    }
                    bitmap = BitmapFactory.decodeFile(mShareLocalImage);
                }
            } else {
                bitmap = mShareBitmap;
            }
        } catch (Exception e) {
            Log.e("ShareContent", e.toString());
        } finally {
            if (null == bitmap) {
                try {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.btn_default);
                } catch (Exception e) {
                    Log.e("ShareContent", e.toString());
                }
            }
        }
        return bitmap;
    }
}

package com.aotuman.share.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
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

    public boolean mNeedAddQRCode = true;

    /**
     * 如果有本地图片分享的时候默认选择本地图片
     */
    public String mShareLocalImage;

    public String mShareNetImage;
    /**
     * 分享形式是h5时候前面的缩略图，调用分享的时候自己设置，默认为ICON
     */
    public String mThumbPath;

    public ShareContentType mShareContentType;

    public ShareRealContent(String title, String content) {
        mShareTitle = title;
        mShareSummary = content;
    }

    public Bitmap getThumbBitmap(Context context) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(mThumbPath)) {
                if (!TextUtils.isEmpty(mShareLocalImage)) {
                    if (mShareLocalImage.startsWith("/")) {
                        mShareLocalImage = "file://" + mShareLocalImage;
                    }
                    bitmap = BitmapFactory.decodeFile(mShareLocalImage);
                } else if (!TextUtils.isEmpty(mShareNetImage)) {
                    if (mShareNetImage.startsWith("/")) {
                        mShareNetImage = "file://" + mShareNetImage;
                    }
                    bitmap = BitmapFactory.decodeFile(mShareNetImage);
                }
            } else {
                if (!TextUtils.isEmpty(mThumbPath)) {
                    if (mThumbPath.startsWith("/")) {
                        mThumbPath = "file://" + mThumbPath;
                    }
                    bitmap = BitmapFactory.decodeFile(mThumbPath);
                }
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

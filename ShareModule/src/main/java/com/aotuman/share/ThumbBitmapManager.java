package com.aotuman.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.aotuman.share.entity.ShareRealContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by aotuman on 2017/7/10.
 */

public class ThumbBitmapManager {
    private final static int WEIXIN_THUMB_SIZE = 120;

    /**
     * 路径获取本地图片
     */
    public Bitmap getBigBitmap(ShareRealContent shareContent) {
        String path = shareContent.mShareLocalImage;
        Bitmap shareBmp = null;
        if(!TextUtils.isEmpty(path)) {
            if (path.startsWith("/")) {
                path = "file://" + path;
            }
            try {
                shareBmp = BitmapFactory.decodeFile(path);
                if (shareBmp != null) {
                    if (getBitmapSize(shareBmp) / 1024 > 5000) {
                        shareBmp = compressImage(shareBmp, 100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return shareBmp;
    }

    /**
     *获取缩略图
     */
    public byte[] getThumbBytes(Bitmap thumbBmp) {
        int height = (int) (WEIXIN_THUMB_SIZE / (float) thumbBmp.getWidth() * thumbBmp.getHeight());
        Bitmap thumbBmpReal = Bitmap.createScaledBitmap(thumbBmp, WEIXIN_THUMB_SIZE, height, true);
        ByteArrayOutputStream bos = compressImageTo32KB(thumbBmpReal);//压缩至32kb
        return bos.toByteArray();
    }

    /**
     * 压缩图片质量至32k,微信分享时缩略图要求
     */
    private ByteArrayOutputStream compressImageTo32KB(Bitmap image) {
        if (image == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 >= 32) {    //循环判断如果压缩后图片是否大于32kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        return baos;
    }

    /**
     * 得到bitmap的大小
     */
    private int getBitmapSize(Bitmap bitmap) {
        if (bitmap == null) {
            return 0;
        }
        return bitmap.getByteCount();
    }


    /**
     * 压缩图片质量
     */
    private Bitmap compressImage(Bitmap image, int size) {
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }
}

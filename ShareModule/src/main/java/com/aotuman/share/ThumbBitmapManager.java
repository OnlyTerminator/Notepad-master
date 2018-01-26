package com.aotuman.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
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
        if (null != shareContent.mShareBitmap) {
            shareBmp = shareContent.mShareBitmap;
        } else {
            if (!TextUtils.isEmpty(path)) {
                if (path.startsWith("/")) {
                    path = "file://" + path;
                }
                try {
                    shareBmp = BitmapFactory.decodeFile(path);
                    if (shareBmp != null) {
                        if (getBitmapSize(shareBmp) / 1024 > 5000) {
                            shareBmp = compressBitmap(shareBmp, 100, 960, 1280);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return shareBmp;
    }

    /**
     * 获取缩略图
     */
    public byte[] getThumbBytes(Bitmap thumbBmp) {
        int height = (int) (WEIXIN_THUMB_SIZE / (float) thumbBmp.getWidth() * thumbBmp.getHeight());
        Bitmap thumbBmpReal = Bitmap.createScaledBitmap(thumbBmp, WEIXIN_THUMB_SIZE, height, true);
        ByteArrayOutputStream bos = compressByteArray(thumbBmpReal, 32, 108, 108);//压缩至32kb
        return bos.toByteArray();
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

    private int getRatioSize(int bitWidth, int bitHeight, int toWidth, int toHeight) {
        // 缩放比
        int ratio = 1;
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > toWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / toWidth;
        } else if (bitWidth < bitHeight && bitHeight > toHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / toHeight;
        }
        // 最小比率为1
        if (ratio <= 0)
            ratio = 1;
        return ratio;
    }

    private ByteArrayOutputStream compressByteArray(Bitmap image, int maxSize, int toWidth, int toHeight) {
        if (null == image) {
            return null;
        }
        // 获取尺寸压缩倍数
        int ratio = getRatioSize(image.getWidth(), image.getHeight(), toWidth, toHeight);
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio, image.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > maxSize && options > 10) {
            // 重置baos即清空baos
            baos.reset();
            // 每次都减少10
            options -= 10;
            // 这里压缩options%，把压缩后的数据存放到baos中
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        return baos;
    }

    private Bitmap compressBitmap(Bitmap image, int maxSize, int toWidth, int toHeight) {
        if (null == image) {
            return null;
        }
        ByteArrayOutputStream baos = compressByteArray(image, maxSize, toWidth, toHeight);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    }
}

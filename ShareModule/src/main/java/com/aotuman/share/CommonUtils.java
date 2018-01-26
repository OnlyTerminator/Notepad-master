package com.aotuman.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by xufeng.zhang on 2017/8/7.
 */

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    /**********************************Screen************************************/

    private static int sScreenWidth;
    private static int sScreenHeight;

    /**
     * 获得屏幕宽度（随屏幕旋转改变）
     * 如果获取失败，则返回720
     *
     * @return 屏幕高度，或720
     */
    public static int getScreenWidth(Context context) {
        if (sScreenWidth != 0) {
            return sScreenWidth;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null && wm.getDefaultDisplay() != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            Display display = wm.getDefaultDisplay();
            display.getMetrics(outMetrics);
            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180://fall through
                    sScreenWidth = outMetrics.widthPixels;
                    sScreenHeight = outMetrics.heightPixels;
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270://fall through
                    sScreenWidth = outMetrics.heightPixels;
                    sScreenHeight = outMetrics.widthPixels;
                    break;
            }
            return sScreenWidth;
        } else {
            return 720;
        }
    }

    /**
     * 获得屏幕高度
     * 如果获取失败，则返回1080
     *
     * @return 屏幕高度，或1080
     */
    public static int getScreenHeight(Context context) {
        if (sScreenHeight != 0) {
            return sScreenHeight;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null && wm.getDefaultDisplay() != null) {
            DisplayMetrics outMetrics = new DisplayMetrics();
            Display display = wm.getDefaultDisplay();
            display.getMetrics(outMetrics);
            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_180://fall through
                    sScreenHeight = outMetrics.heightPixels;
                    sScreenWidth = outMetrics.widthPixels;
                    break;
                case Surface.ROTATION_90:
                case Surface.ROTATION_270://fall through
                    sScreenHeight = outMetrics.widthPixels;
                    sScreenWidth = outMetrics.heightPixels;
                    break;
            }
            return sScreenHeight;
        } else {
            return 1080;
        }
    }

    public static File getFilesDir(Context context, String folder) {
        if (context == null) {
            return null;
        }
        File file = context.getExternalFilesDir(folder);
        if (null == file) {
            file = context.getFilesDir();
        }
        return file;
    }

    public static boolean writeBitmap(File file, Bitmap img, int quality, boolean deleteExist) {
        FileOutputStream fos = null;
        if (null == file || null == img || img.isRecycled()) {
            return false;
        }
        boolean ret = false;
        try {
            if (!deleteExist && file.exists()) {
                return false;
            }
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                Log.i(TAG, "writeBitmap make dirs failed");
            }
            if (file.exists() && !file.delete()) {
                Log.i(TAG, "writeBitmap delete old failed");
            }

            fos = new FileOutputStream(file);
            ret = img.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            ret = false;
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        return ret;
    }
}

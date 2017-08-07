package com.aotuman.notepad.base.utils;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.aotuman.notepad.base.NotepadApplication;

import java.io.File;

/**
 * Created by xufeng.zhang on 2017/8/7.
 */

public class CommonUtils {
    private static Context mAppContext;

    static {
        mAppContext = NotepadApplication.getAppContext();
    }

    /**********************************Screen************************************/

    private static int sScreenWidth;
    private static int sScreenHeight;

    /**
     * 获得屏幕宽度（随屏幕旋转改变）
     * 如果获取失败，则返回720
     *
     * @return 屏幕高度，或720
     */
    public static int getScreenWidth() {
        if (sScreenWidth != 0) {
            return sScreenWidth;
        }
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
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
    public static int getScreenHeight() {
        if (sScreenHeight != 0) {
            return sScreenHeight;
        }
        WindowManager wm = (WindowManager) mAppContext.getSystemService(Context.WINDOW_SERVICE);
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

    /**********************************单位转化************************************/

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mAppContext.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, mAppContext.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(float pxVal) {
        final float scale = mAppContext.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        return (pxVal / mAppContext.getResources().getDisplayMetrics().scaledDensity);
    }
    /**********************************SDCard************************************/

    /**
     * 判断SDCard是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

}

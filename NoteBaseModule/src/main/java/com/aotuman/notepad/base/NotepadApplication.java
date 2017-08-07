package com.aotuman.notepad.base;

import android.content.Context;

/**
 * Created by xufeng.zhang on 2017/8/7.
 */

public class NotepadApplication {
    /**
     * it will be init by initContext() method when application start to work
     */
    private static Context mAppContext;

    public static Context getAppContext() {
        if (null == mAppContext) {
            throw new RuntimeException("must call method initContext");
        }
        return mAppContext;
    }


    /**
     * must execute this method when application onCreate
     * @param context
     */
    public static void initContext(Context context) {
        mAppContext = context;
    }
}

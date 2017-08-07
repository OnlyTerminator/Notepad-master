package com.aotuman.notepad;

import android.app.Application;
import android.content.Context;

import com.aotuman.notepad.base.NotepadApplication;

/**
 * Created by 凹凸曼 on 2016/12/7.
 */

public class ATMApplication extends Application{
    private static ATMApplication instance;
    public static ATMApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        NotepadApplication.initContext(base);
    }
}

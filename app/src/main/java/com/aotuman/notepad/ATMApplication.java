package com.aotuman.notepad;

import android.app.Application;

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
}

package com.aotuman.notepad;

import android.app.Application;
import android.content.Context;

import com.aotuman.notepad.base.NotepadApplication;
import com.aotuman.share.entity.ShareNewConfig;

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
        ShareNewConfig.initShareKey("wxbe5772231d5d0bfe","2b51112a0468664ec2c9f26422a867d3","2197263330","1106131934","");
    }
}

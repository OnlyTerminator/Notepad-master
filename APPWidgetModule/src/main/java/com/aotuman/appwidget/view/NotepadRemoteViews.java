package com.aotuman.appwidget.view;

import android.content.Context;
import android.widget.RemoteViews;

import com.aotuman.appwidget.NotePadAppWidgetProvider;

/**
 * Created by xufeng.zhang on 2017/8/5.
 */

public abstract class NotepadRemoteViews extends RemoteViews {
    private Class<? extends NotePadAppWidgetProvider> mAWProvider;
    public NotepadRemoteViews(Context context, int layoutId, Class<? extends NotePadAppWidgetProvider> clz) {
        super(context.getPackageName(), layoutId);
        mAWProvider = clz;
        setHotspotAction(context);
    }

    public abstract void setHotspotAction(Context context);

    public abstract void updateView();
    public Class<? extends NotePadAppWidgetProvider> getProviderClz(){
        return mAWProvider;
    }
}

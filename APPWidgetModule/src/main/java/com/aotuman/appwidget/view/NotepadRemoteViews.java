package com.aotuman.appwidget.view;

import android.content.Context;
import android.widget.RemoteViews;

import com.aotuman.appwidget.NotePadAppWidgetProvider;

/**
 * Created by xufeng.zhang on 2017/8/5.
 */

public abstract class NotepadRemoteViews extends RemoteViews {
    protected static final String NEXT_NOTEPAD = "com.aotuman.next.notepad";
    protected static final String TOP_NOTEPAD = "com.aotuman.top.notepad";
    public NotepadRemoteViews(Context context, int layoutId) {
        super(context.getPackageName(), layoutId);
        setHotspotAction(context);
    }

    public abstract void setHotspotAction(Context context);

    public abstract void updateView(Context context);

    public abstract void changeView(Context context,int type); //0表示上一篇 1表示下一篇
}

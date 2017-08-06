package com.aotuman.appwidget.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aotuman.appwidget.R;

/**
 * Created by xufeng.zhang on 2017/8/5.
 */

public class NotepadWidgetView4X1 extends NotepadRemoteViews {
    public NotepadWidgetView4X1(Context context) {
        super(context, R.layout.notepad_appwidget_4x1);
    }

    @Override
    public void setHotspotAction(Context context) {
        Intent addNotepad = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent launcherPendingIntent = PendingIntent.getActivity(context, 41, addNotepad, PendingIntent.FLAG_UPDATE_CURRENT);
        setOnClickPendingIntent(R.id.iv_add, launcherPendingIntent);
        setOnClickPendingIntent(R.id.tv_add_notepad,launcherPendingIntent);
    }

    @Override
    public void updateView(Context context) {}

    @Override
    public void changeView(Context context,int type) {

    }
}

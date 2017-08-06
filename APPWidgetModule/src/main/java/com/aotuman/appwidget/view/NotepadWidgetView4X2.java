package com.aotuman.appwidget.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.aotuman.appwidget.NotePadAppWidgetProvider;
import com.aotuman.appwidget.NotepadWidget4X1;
import com.aotuman.appwidget.NotepadWidget4X2;
import com.aotuman.appwidget.R;
import com.aotuman.notepad.base.database.NotepadDataManager;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.aotuman.notepad.base.utils.TimeUtils;

import java.util.List;

/**
 * Created by xufeng.zhang on 2017/8/5.
 */

public class NotepadWidgetView4X2 extends NotepadRemoteViews {

    public NotepadWidgetView4X2(Context context) {
        super(context, R.layout.notepad_appwidget_4x2);
    }

    @Override
    public void setHotspotAction(Context context) {
        Intent addNotepad = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent launcherPendingIntent = PendingIntent.getActivity(context, 42, addNotepad, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent nextIntent = new Intent(context, NotepadWidget4X2.class);
        nextIntent.setAction(NEXT_NOTEPAD);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 42, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent topIntent = new Intent(context, NotepadWidget4X2.class);
        topIntent.setAction(TOP_NOTEPAD);
        PendingIntent topPendingIntent = PendingIntent.getBroadcast(context, 42, topIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        setOnClickPendingIntent(R.id.iv_add, launcherPendingIntent);
        setOnClickPendingIntent(R.id.tv_add_notepad, launcherPendingIntent);
        setOnClickPendingIntent(R.id.tv_notepad_next,nextPendingIntent);
        setOnClickPendingIntent(R.id.tv_notepad_top,topPendingIntent);
    }

    @Override
    public void updateView(Context context) {
        List<NotepadContentInfo> list = NotepadDataManager.getInstance(context).findAllNotepad();
        if (null != list && !list.isEmpty()) {
            NotepadContentInfo info = list.get(0);
            if (null != info) {
                String title = info.title;
                String content = info.content;
                String time = info.time;
                if(TextUtils.isEmpty(title)){
                    title = "主人没有给我标题！";
                }
                if(TextUtils.isEmpty(content)){
                    content = "震惊！我竟然是没有内容的便签";
                }
                if(TextUtils.isEmpty(time)){
                    time = String.valueOf(System.currentTimeMillis());
                }
                setTextViewText(R.id.tv_notepad_time, TimeUtils.timeStampToDateAndHour(Long.parseLong(time)));
                setTextViewText(R.id.tv_notepad_title, title);
                setTextViewText(R.id.tv_notepad_content, content);
            }
            SPUtils.put(context, SharePreEvent.APPWIDGET_SELECT,0);
        } else {
            setTextViewText(R.id.tv_notepad_title, "天哪！你对自己尽然没有规划");
            setTextViewText(R.id.tv_notepad_content, "快去添加便签，开始规划自己的未来吧");
        }
    }

    @Override
    public void changeView(Context context,int type) {
        List<NotepadContentInfo> list = NotepadDataManager.getInstance(context).findAllNotepad();
        int index = (int)SPUtils.get(context,SharePreEvent.APPWIDGET_SELECT,0);
        if (null != list && !list.isEmpty()) {
            if(type == 1) {
                ++index;
                if (index >= list.size()) {
                    index = 0;
                }
            }else if(type == 0){
                --index;
                if (index < 0) {
                    index = 0;
                    Toast.makeText(context,"没有更新的便签了！",Toast.LENGTH_SHORT).show();
                }
            }else {
                return;
            }
            SPUtils.put(context, SharePreEvent.APPWIDGET_SELECT,index);
            NotepadContentInfo info = list.get(index);
            if (null != info) {
                String title = info.title;
                String content = info.content;
                String time = info.time;
                if(TextUtils.isEmpty(title)){
                    title = "主人没有给我标题！";
                }
                if(TextUtils.isEmpty(content)){
                    content = "震惊！我竟然是没有内容的便签";
                }
                if(TextUtils.isEmpty(time)){
                    time = String.valueOf(System.currentTimeMillis());
                }
                setTextViewText(R.id.tv_notepad_time, TimeUtils.timeStampToDateAndHour(Long.parseLong(time)));
                setTextViewText(R.id.tv_notepad_title, title);
                setTextViewText(R.id.tv_notepad_content, content);
            }
        }
    }
}

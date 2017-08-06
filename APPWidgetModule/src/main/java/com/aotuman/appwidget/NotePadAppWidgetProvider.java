package com.aotuman.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by xufeng.zhang on 2017/6/24.
 */

public abstract class NotePadAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "NotePadAppWidget";

    // 保存 widget 的id的HashSet，每新建一个 widget 都会为该 widget 分配一个 id。
    private static Map<Integer,Integer> idsSet = new HashMap<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "onUpdate: "+appWidgetIds.length);
        // 每次 widget 被创建时，对应的将widget的id添加到set中
        for (int appWidgetId : appWidgetIds) {
            idsSet.put(Integer.valueOf(appWidgetId),widgetType());
        }
        NotePadAppWidgetManager.getInstance().updateAllWidget(context,idsSet);
    }

    /**
     * 第一个桌面插件添加的时候会被调用
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // 当 widget 被删除时，对应的删除set中保存的widget的id
        for (int appWidgetId : appWidgetIds) {
            idsSet.remove(Integer.valueOf(appWidgetId));
        }
    }

    /**
     * 最后一个桌面插件被删除的时候调用
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        if (TextUtils.isEmpty(action) || null == context) {
            return;
        }
        if("com.aotuman.next.notepad".equals(action)){
            NotePadAppWidgetManager.getInstance().changeWidget(context,idsSet,1);
        }else if("com.aotuman.top.notepad".equals(action)){
            NotePadAppWidgetManager.getInstance().changeWidget(context,idsSet,0);
        }else {
            NotePadAppWidgetManager.getInstance().updateAllWidget(context,idsSet);
        }
    }

    protected abstract int widgetType();
}

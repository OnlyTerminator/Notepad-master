package com.aotuman.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;

import com.aotuman.appwidget.view.NotepadRemoteViews;
import com.aotuman.appwidget.view.NotepadWidgetView4X1;
import com.aotuman.appwidget.view.NotepadWidgetView4X2;

import java.util.Map;
import java.util.Set;

/**
 * Created by xufeng.zhang on 2017/8/6.
 */

public class NotePadAppWidgetManager {
    private static final String TAG = "NotepadWidgetManager";
    private static NotePadAppWidgetManager instance;

    public NotePadAppWidgetManager() {
    }

    public synchronized static NotePadAppWidgetManager getInstance() {
        if (null == instance) {
            instance = new NotePadAppWidgetManager();
        }
        return instance;
    }

    public void updateAllWidget(Context context, Map<Integer, Integer> widgets) {
        if(null == widgets){
            return;
        }
        Set<Integer> set = widgets.keySet(); //得到所有key的集合
        if(null != set) {
            for (Integer in : set) {
                Integer value = widgets.get(in);
                System.out.println(in + "     " + value);
                NotepadRemoteViews remoteViews;
                if (value == 41) {
                    remoteViews = new NotepadWidgetView4X1(context);
                } else {
                    remoteViews = new NotepadWidgetView4X2(context);
                }

                remoteViews.updateView(context);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                try {
                    manager.updateAppWidget(in, remoteViews);
                } catch (RuntimeException e) {
                    Log.e("AWUpdateStrategy", "if IAppWidgetService dead will throw this Exception");
                }
            }
        }

    }

    public void changeWidget(Context context, Map<Integer, Integer> widgets,int type){
        if(null == widgets){
            return;
        }
        Set<Integer> set = widgets.keySet(); //得到所有key的集合
        if(null != set) {
            for (Integer in : set) {
                Integer value = widgets.get(in);
                System.out.println(in + "     " + value);
                if (value == 42) {
                    NotepadRemoteViews remoteViews = new NotepadWidgetView4X2(context);
                    remoteViews.changeView(context,type);
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    try {
                        manager.updateAppWidget(in, remoteViews);
                    } catch (RuntimeException e) {
                        Log.e("AWUpdateStrategy", "if IAppWidgetService dead will throw this Exception");
                    }
                }
            }
        }
    }
}

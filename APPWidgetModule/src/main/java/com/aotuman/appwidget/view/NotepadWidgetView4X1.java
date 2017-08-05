package com.aotuman.appwidget.view;

import android.content.Context;

import com.aotuman.appwidget.NotepadWidget4X1;
import com.aotuman.appwidget.R;
import com.aotuman.notepad.base.database.NotepadDataManager;
import com.aotuman.notepad.base.entry.NotepadContentInfo;

import java.util.List;

/**
 * Created by xufeng.zhang on 2017/8/5.
 */

public class NotepadWidgetView4X1 extends NotepadRemoteViews {
    private Context mContext;
    public NotepadWidgetView4X1(Context context) {
        super(context, R.layout.notepad_appwidget_4x1, NotepadWidget4X1.class);
        mContext = context;
    }

    @Override
    public void setHotspotAction(Context context) {

    }

    @Override
    public void updateView() {
        List<NotepadContentInfo> list = NotepadDataManager.getInstance(mContext).findAllNotepad();
    }
}

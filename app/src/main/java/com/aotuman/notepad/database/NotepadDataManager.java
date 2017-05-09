package com.aotuman.notepad.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.notepad.entry.NotepadContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/9.
 */

public class NotepadDataManager {
    private static NotepadDataBaseHelp mNotepadDataBaseHelp = null;
    private static NotepadDataManager instance;
    private Context context;

    public NotepadDataManager(Context context) {
        this.context = context;
        mNotepadDataBaseHelp = new NotepadDataBaseHelp(context);
    }

    public static NotepadDataManager getInstance(Context context) {
        if (null == instance) {
            instance = new NotepadDataManager(context);
        }
        return instance;
    }

    public void insertNotepadInfo(NotepadContentInfo info) {
        String sql = "insert into notepadinfo (title,content,notegroup,time)values(?,?,?,?)";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.title, info.content, info.group, info.time});
        db.close();
    }

    public void updateNotepadTitle(String title,long time) {
        String sql = "update notepadinfo set title = ?,time = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title,String.valueOf(time)});
        db.close();
    }

    public void updateNotepadContent(String content,long time) {
        String sql = "update notepadinfo set content = ?,time = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{content,String.valueOf(time)});
        db.close();
    }

    public void updateNotepadTitleAndContent(String title,String content,long time) {
        String sql = "update notepadinfo set title = ?,content = ?,time = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title,content,String.valueOf(time)});
        db.close();
    }

    public void deleteNotepadInfo(NotepadContentInfo info) {
        String sql = "delete from notepadinfo where content = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.content});
        db.close();
    }

    public List<NotepadContentInfo> findAllNotepad() {
        List<NotepadContentInfo> notepadContentInfos = new ArrayList<>();
        String sql = "select * from notepadinfo";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                NotepadContentInfo notepadContentInfo = new NotepadContentInfo();
                notepadContentInfo.title = cursor.getString(1);
                notepadContentInfo.content = cursor.getString(2);
                notepadContentInfo.group = cursor.getString(3);
                notepadContentInfo.time = cursor.getString(4);
                notepadContentInfos.add(notepadContentInfo);
            }
            cursor.close();
        }
        db.close();
        return notepadContentInfos;
}
}

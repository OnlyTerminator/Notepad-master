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

    public NotepadDataManager(Context context) {
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

    public void updateNotepadTitle(int id,String title,long time) {
        String sql = "update notepadinfo set title = ?,time = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title,String.valueOf(time),id});
        db.close();
    }

    public void updateNotepadContent(int id,String content,long time) {
        String sql = "update notepadinfo set content = ?,time = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{content,String.valueOf(time),id});
        db.close();
    }

    public void updateNotepadTitleAndContent(int id,String title,String content,long time) {
        String sql = "update notepadinfo set title = ?,content = ?,time = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title,content,String.valueOf(time),id});
        db.close();
    }

    public void deleteNotepadInfo(int id) {
        String sql = "delete from notepadinfo where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{id});
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
                notepadContentInfo.id = cursor.getInt(0);
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

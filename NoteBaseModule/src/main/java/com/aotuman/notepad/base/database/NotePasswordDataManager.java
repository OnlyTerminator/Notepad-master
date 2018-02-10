package com.aotuman.notepad.base.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.entry.PasswordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/9.
 */

public class NotePasswordDataManager {
    private static NotePasswordDataBaseHelp mNotePasswordDataBaseHelp = null;
    private static NotePasswordDataManager instance;
    private Context context;
    private static final String mTabName = "passwordinfo";
    public NotePasswordDataManager(Context context) {
        this.context = context;
        mNotePasswordDataBaseHelp = new NotePasswordDataBaseHelp(context);
    }

    public static NotePasswordDataManager getInstance(Context context) {
        if (null == instance) {
            instance = new NotePasswordDataManager(context);
        }
        return instance;
    }

    public void insertPasswordInfo(PasswordInfo info) {
//        String sql = "insert into passwordinfo (groups,title,name,password)values(?,?,?,?)";
        ContentValues contentValues = new ContentValues();
        contentValues.put("groups",info.groups);
        contentValues.put("title",info.title);
        contentValues.put("name",info.name);
        contentValues.put("password",info.password);
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        int count = db.update(mTabName,contentValues,"title=? and groups=?",new String []{info.title,info.groups});
        if(count <= 0){
            db.insert(mTabName,null,contentValues);
        }
        db.close();
    }

//    public void updatePasswordInfo(String title,String name,String password) {
//        String sql = "update passwordinfo set name = ? and password = ? where title = ?";
//        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
//        db.update()
//        db.execSQL(sql, new Object[]{name,password,title});
//        db.close();
//    }

    public void deletePasswordInfo(String title,String groups) {
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        db.delete(mTabName,"title=? and groups=?",new String[]{title,groups});
        db.close();
    }

    public void deleteGroupPasswordInfo(String groups) {
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        db.delete(mTabName,"groups=?",new String[]{groups});
        db.close();
    }

    public List<PasswordInfo> findAllPassword(String groups) {
        List<PasswordInfo> passwordInfos = new ArrayList<>();
        String sql = "select * from "+mTabName+" where groups = ?";
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{groups});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                PasswordInfo notepadContentInfo = new PasswordInfo();
                notepadContentInfo.groups = cursor.getString(1);
                notepadContentInfo.title = cursor.getString(2);
                notepadContentInfo.name = cursor.getString(3);
                notepadContentInfo.password = cursor.getString(4);
                passwordInfos.add(notepadContentInfo);
            }
            cursor.close();
        }
        db.close();
        return passwordInfos;
    }
}

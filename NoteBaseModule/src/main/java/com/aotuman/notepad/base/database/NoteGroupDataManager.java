package com.aotuman.notepad.base.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.notepad.base.entry.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/9.
 */

public class NoteGroupDataManager {
    private static NoteGroupDataBaseHelp mNoteGroupDataBaseHelp = null;
    private static NoteGroupDataManager instance;
    private Context context;

    public NoteGroupDataManager(Context context) {
        this.context = context;
        mNoteGroupDataBaseHelp = new NoteGroupDataBaseHelp(context);
    }

    public static NoteGroupDataManager getInstance(Context context) {
        if (null == instance) {
            instance = new NoteGroupDataManager(context);
        }
        return instance;
    }

    public void insertGroupInfo(GroupInfo info) {
        String sql = "insert into groupinfo (name,count)values(?,?)";
        SQLiteDatabase db = mNoteGroupDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.groupName,info.groupCount});
        db.close();
    }

    public void initGroupInfos(List<GroupInfo> infos) {
        if(null != infos && !infos.isEmpty()){
            for (int i = 0; i < infos.size(); i++){
                GroupInfo in = infos.get(i);
                if(null != in) {
                    insertGroupInfo(in);
                }
            }
        }
    }

    public void updateGroupInfo(String name,long count) {
        String sql = "update groupinfo set count = ? where name = ?";
        SQLiteDatabase db = mNoteGroupDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{count,name});
        db.close();
    }

    public void updateGroupName(String nowName,String lastName) {
        String sql = "update groupinfo set name = ? where name = ?";
        SQLiteDatabase db = mNoteGroupDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{nowName,lastName});
        db.close();
    }
    public void deleteNotepadInfo(String name) {
        String sql = "delete from groupinfo where name = ?";
        SQLiteDatabase db = mNoteGroupDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{name});
        db.close();
    }

    public List<GroupInfo> findAllNotepad() {
        List<GroupInfo> groupInfos = new ArrayList<>();
        String sql = "select * from groupinfo";
        SQLiteDatabase db = mNoteGroupDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                GroupInfo notepadContentInfo = new GroupInfo();
                notepadContentInfo.groupName = cursor.getString(1);
                notepadContentInfo.groupCount = cursor.getInt(2);
                groupInfos.add(notepadContentInfo);
            }
            cursor.close();
        }
        db.close();
        return groupInfos;
}
}

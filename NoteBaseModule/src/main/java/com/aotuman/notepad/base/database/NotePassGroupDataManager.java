package com.aotuman.notepad.base.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.entry.PassGroupInfo;
import com.aotuman.notepad.base.entry.PasswordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/9.
 */

public class NotePassGroupDataManager {
    private static NotePassGroupDataBaseHelp mNotePasswordDataBaseHelp = null;
    private static NotePassGroupDataManager instance;
    private Context context;

    public NotePassGroupDataManager(Context context) {
        this.context = context;
        mNotePasswordDataBaseHelp = new NotePassGroupDataBaseHelp(context);
    }

    public static NotePassGroupDataManager getInstance(Context context) {
        if (null == instance) {
            instance = new NotePassGroupDataManager(context);
        }
        return instance;
    }

    public void insertPassgroupInfo(PassGroupInfo info) {
        String sql = "insert into passgroupinfo (groups)values(?)";
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.groups});
        db.close();
    }

    public void deletePassgroupInfo(String name) {
        String sql = "delete from passgroupinfo where groups = ?";
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{name});
        db.close();
    }

    public List<PassGroupInfo> findAllPasswordGroups(String groups) {
        List<PassGroupInfo> passGroupInfos = new ArrayList<>();
        String sql = "select * from passgroupinfo";
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{groups});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                PassGroupInfo passGroupInfo = new PassGroupInfo();
                passGroupInfo.groups = cursor.getString(1);
                passGroupInfos.add(passGroupInfo);
            }
            cursor.close();
        }
        db.close();
        return passGroupInfos;
    }
    public List<NotepadContentInfo> findAllPasswordGroups2(String groups) {
        List<NotepadContentInfo> passGroupInfos = new ArrayList<>();
        String sql = "select * from passgroupinfo";
        SQLiteDatabase db = mNotePasswordDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{});
        if(null != cursor) {
            while (cursor.moveToNext()) {
                NotepadContentInfo passGroupInfo = new NotepadContentInfo();
                passGroupInfo.title = cursor.getString(1);
                passGroupInfo.group = "密码";
                passGroupInfos.add(passGroupInfo);
            }
            cursor.close();
        }
        db.close();
        return passGroupInfos;
    }
}

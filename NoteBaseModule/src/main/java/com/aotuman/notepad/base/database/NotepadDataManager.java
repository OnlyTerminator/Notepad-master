package com.aotuman.notepad.base.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aotuman.notepad.base.entry.NotepadContentInfo;

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

    public void insertNotepadInfo(final NotepadContentInfo info) {
        String sql = "insert into notepadinfo (title,content,notegroup,time,images)values(?,?,?,?,?)";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{info.title, info.content, info.group, info.time,info.imageLists});
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.createObject(NotepadContentInfo.class,info);
//            }
//        });
//        realm.close();
    }

    public void updateNotepadTitle(final int id, final String title, final long time, String images) {
        String sql = "update notepadinfo set title = ?,time = ?, images = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title, String.valueOf(time), images,id});
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //先查找后得到User对象
//                NotepadContentInfo user = realm.where(NotepadContentInfo.class).equalTo("id",id).findFirst();
//                user.title = title;
//                user.time = String.valueOf(time);
//            }
//        });
//        realm.close();
    }

    public void updateNotepadPic(final int id, final long time, final String images) {
        String sql = "update notepadinfo set time = ?, images = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{String.valueOf(time), images,id});
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //先查找后得到User对象
//                NotepadContentInfo user = realm.where(NotepadContentInfo.class).equalTo("id",id).findFirst();
//                user.imageLists = images;
//                user.time = String.valueOf(time);
//            }
//        });
//        realm.close();
    }

    public void updateNotepadContent(final int id, final String content, final long time, final String images) {
        String sql = "update notepadinfo set content = ?,time = ?,images = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{content, String.valueOf(time), images,id});
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //先查找后得到User对象
//                NotepadContentInfo user = realm.where(NotepadContentInfo.class).equalTo("id",id).findFirst();
//                user.imageLists = images;
//                user.time = String.valueOf(time);
//                user.content = content;
//            }
//        });
//        realm.close();
    }

    public void updateNotepadTitleAndContent(final int id, final String title, final String content, final long time,final String images) {
        String sql = "update notepadinfo set title = ?,content = ?,time = ?,images = ? where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{title, content, String.valueOf(time), images,id});
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                //先查找后得到User对象
//                NotepadContentInfo user = realm.where(NotepadContentInfo.class).equalTo("id",id).findFirst();
//                user.imageLists = images;
//                user.time = String.valueOf(time);
//                user.content = content;
//                user.title = title;
//            }
//        });
//        realm.close();
    }

    public void deleteNotepadInfo(int id) {
        String sql = "delete from notepadinfo where id = ?";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        db.execSQL(sql, new Object[]{id});
        db.close();
        //先查找到数据
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        final RealmResults<NotepadContentInfo> notepadContentInfos = realm.where(NotepadContentInfo.class).equalTo("id",id).findAll();
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                notepadContentInfos.deleteAllFromRealm();
//            }
//        });
//        realm.close();
    }

    public List<NotepadContentInfo> findAllNotepad() {
        List<NotepadContentInfo> notepadContentInfos = new ArrayList<>();
        String sql = "select * from notepadinfo order by time desc";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{});
        if (null != cursor) {
            while (cursor.moveToNext()) {
                NotepadContentInfo notepadContentInfo = new NotepadContentInfo();
                notepadContentInfo.id = cursor.getInt(0);
                notepadContentInfo.title = cursor.getString(1);
                notepadContentInfo.content = cursor.getString(2);
                notepadContentInfo.group = cursor.getString(3);
                notepadContentInfo.time = cursor.getString(4);
                notepadContentInfo.imageLists = cursor.getString(5);
                notepadContentInfos.add(notepadContentInfo);
            }
            cursor.close();
        }
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        RealmResults<NotepadContentInfo> notepadContentInfos = realm.where(NotepadContentInfo.class).findAll();
//        realm.close();
        return notepadContentInfos;
    }

    public List<NotepadContentInfo> findNotepadByGroup(String groupName) {
        List<NotepadContentInfo> notepadContentInfos = new ArrayList<>();
        String sql = "select * from notepadinfo where notegroup = ?  order by time desc";
        SQLiteDatabase db = mNotepadDataBaseHelp.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(sql, new String[]{groupName});
        if (null != cursor) {
            while (cursor.moveToNext()) {
                NotepadContentInfo notepadContentInfo = new NotepadContentInfo();
                notepadContentInfo.id = cursor.getInt(0);
                notepadContentInfo.title = cursor.getString(1);
                notepadContentInfo.content = cursor.getString(2);
                notepadContentInfo.group = cursor.getString(3);
                notepadContentInfo.time = cursor.getString(4);
                notepadContentInfo.imageLists = cursor.getString(5);
                notepadContentInfos.add(notepadContentInfo);
            }
            cursor.close();
        }
        db.close();
//        Realm realm = NoteDatabaseHelper.getInstance().getNotepadRealm();
//        RealmResults<NotepadContentInfo> notepadContentInfos = realm.where(NotepadContentInfo.class).equalTo("group",groupName).findAll();
//        notepadContentInfos.sort("time", Sort.DESCENDING);
//        realm.close();
        return notepadContentInfos;
    }
}

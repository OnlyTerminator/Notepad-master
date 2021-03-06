package com.aotuman.notepad.base.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aotuman on 2017/5/10.
 */

public class NotePasswordDataBaseHelp extends SQLiteOpenHelper {
    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String name = "password_info"; //数据库名称

    private static final int version = 1; //数据库版本

    public NotePasswordDataBaseHelp(Context context) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS passwordinfo (id integer primary key autoincrement, groups TEXT,title TEXT,name TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

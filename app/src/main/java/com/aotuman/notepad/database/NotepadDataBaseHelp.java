package com.aotuman.notepad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aotuman on 2017/5/9.
 */

public class NotepadDataBaseHelp extends SQLiteOpenHelper {
//类没有实例化,是不能用作父类构造器的参数,必须声明为静态

    private static final String name = "notepad_info"; //数据库名称

    private static final int version = 1; //数据库版本

    public NotepadDataBaseHelp(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notepadinfo (id integer primary key autoincrement, title TEXT, content TEXT, notegroup TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if(newVersion > oldVersion){
//            // Drop tables
//            db.execSQL("DROP TABLE IF EXISTS " + name);
//            // Create tables
//            onCreate(db);
//        }
    }
}

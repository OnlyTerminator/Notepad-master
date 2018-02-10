//package com.aotuman.notepad.base.database;
//
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//
///**
// * Created by aotuman on 2018/2/3.
// */
//
//public class NoteDatabaseHelper {
//    private static NoteDatabaseHelper sInstance = new NoteDatabaseHelper();
//    private RealmConfiguration mNotePadConfig;
//    private RealmConfiguration mNoteGroupConfig;
//    private RealmConfiguration mNotePassConfig;
//    private NoteDatabaseHelper(){
//         mNotePadConfig = new RealmConfiguration.Builder()
//                .name("note_pad.realm") //文件名
//                .schemaVersion(0) //版本号
//                .build();
//
//        mNoteGroupConfig = new RealmConfiguration.Builder()
//                .name("note_group.realm") //文件名
//                .schemaVersion(0) //版本号
//                .build();
//
//        mNotePassConfig = new RealmConfiguration.Builder()
//                .name("note_pass.realm") //文件名
//                .schemaVersion(0) //版本号
//                .build();
//    }
//
//    public static NoteDatabaseHelper getInstance(){
//        return sInstance;
//    }
//
//    public Realm getNotepadRealm(){
//        return Realm.getInstance(mNotePadConfig);
//    }
//
//    public Realm getNotegroupRealm(){
//        return Realm.getInstance(mNoteGroupConfig);
//    }
//
//    public Realm getNotePassRealm(){
//        return Realm.getInstance(mNotePassConfig);
//    }
//}

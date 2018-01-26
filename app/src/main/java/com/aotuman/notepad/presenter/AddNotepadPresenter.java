package com.aotuman.notepad.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.entry.NotepadContentInfo;

/**
 * Created by aotuman on 2018/1/25.
 */

public class AddNotepadPresenter extends NotePresenter<AddNotepadPresenter.AddNotepadCallback> {
    public AddNotepadPresenter(AddNotepadCallback callback) {
        super(callback);
    }
    public void initData(Intent intent){
        if(null != intent){
            String type = intent.getStringExtra("type");
            if(!TextUtils.isEmpty(type)){
                NotepadContentInfo info = (NotepadContentInfo) intent.getSerializableExtra("notepad");
                mCallback.createAddNotepadView(info);
                return;
            }
        }
        mCallback.createAddNotepadView(null);
    }
    public interface AddNotepadCallback extends NotePresenter.ICallback{
        void createAddNotepadView(NotepadContentInfo info);
    }
}

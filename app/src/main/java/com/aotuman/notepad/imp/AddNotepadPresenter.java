package com.aotuman.notepad.imp;

import android.content.Intent;
import android.text.TextUtils;

import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.define.IAddNotepadPresenter;
import com.aotuman.notepad.define.IAddNotepadView;

public class AddNotepadPresenter implements IAddNotepadPresenter {

    private AddNotepadModel mModel;
    private IAddNotepadView mView;

    public AddNotepadPresenter(IAddNotepadView mView) {
        this.mView = mView;
        mModel = new AddNotepadModel(this);
    }

    @Override
    public void initData(Intent intent) {
        if(null != intent){
            String type = intent.getStringExtra("type");
            if(!TextUtils.isEmpty(type)){
                NotepadContentInfo info = (NotepadContentInfo) intent.getSerializableExtra("notepad");
                mView.createAddNotepadView(info);
                return;
            }
        }
        mView.createAddNotepadView(null);
    }
}
package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IAddNotepadPresenter;
import com.aotuman.notepad.define.IAddNotepadView;

public class AddNotepadPresenter implements IAddNotepadPresenter {

    private AddNotepadModel mModel;
    private IAddNotepadView mView;

    public AddNotepadPresenter(IAddNotepadView mView) {
        this.mView = mView;
        mModel = new AddNotepadModel(this);
    }
}
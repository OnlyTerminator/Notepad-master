package com.aotuman.notepad.imp;


import com.aotuman.notepad.define.IEditGroupPresenter;
import com.aotuman.notepad.define.IEditGroupView;

public class EditGroupPresenter implements IEditGroupPresenter {

    private EditGroupModel mModel;
    private IEditGroupView mView;

    public EditGroupPresenter(IEditGroupView mView) {
        this.mView = mView;
        mModel = new EditGroupModel(this);
    }
}
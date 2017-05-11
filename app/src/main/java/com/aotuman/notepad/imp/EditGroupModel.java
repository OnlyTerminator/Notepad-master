package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IEditGroupModel;
import com.aotuman.notepad.define.IEditGroupPresenter;

public class EditGroupModel implements IEditGroupModel {

    private IEditGroupPresenter presenter;

    public EditGroupModel(IEditGroupPresenter presenter) {
        this.presenter = presenter;
    }
}
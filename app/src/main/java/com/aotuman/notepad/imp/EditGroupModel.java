package com.aotuman.notepad.imp;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.define.IEditGroupModel;
import com.aotuman.notepad.define.IEditGroupPresenter;

import java.util.List;

public class EditGroupModel implements IEditGroupModel {

    private IEditGroupPresenter presenter;

    public EditGroupModel(IEditGroupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<GroupInfo> getGroupInfo() {
        return NoteGroupDataManager.getInstance(ATMApplication.getInstance()).findAllNotepad();
    }
}
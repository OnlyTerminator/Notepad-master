package com.aotuman.notepad.imp;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.database.NoteGroupDataManager;
import com.aotuman.notepad.define.IEditGroupModel;
import com.aotuman.notepad.define.IEditGroupPresenter;
import com.aotuman.notepad.entry.GroupInfo;

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
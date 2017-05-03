package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IAddNotepadModel;
import com.aotuman.notepad.define.IAddNotepadPresenter;

public class AddNotepadModel implements IAddNotepadModel {

    private IAddNotepadPresenter presenter;

    public AddNotepadModel(IAddNotepadPresenter presenter) {
        this.presenter = presenter;
    }
}
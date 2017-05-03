package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IMainModel;
import com.aotuman.notepad.define.IMainPresenter;

public class MainModel implements IMainModel {

    private IMainPresenter presenter;

    public MainModel(IMainPresenter presenter) {
        this.presenter = presenter;
    }
}
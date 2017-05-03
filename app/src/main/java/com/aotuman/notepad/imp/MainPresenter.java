package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IMainPresenter;
import com.aotuman.notepad.define.IMainView;

public class MainPresenter implements IMainPresenter {

    private MainModel mModel;
    private IMainView mView;

    public MainPresenter(IMainView mView) {
        this.mView = mView;
        mModel = new MainModel(this);
    }
}
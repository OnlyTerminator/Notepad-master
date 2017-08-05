package com.aotuman.notepad.imp;

import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.define.IMainPresenter;
import com.aotuman.notepad.define.IMainView;

import java.util.List;

public class MainPresenter implements IMainPresenter {

    private MainModel mModel;
    private IMainView mView;

    public MainPresenter(IMainView mView) {
        this.mView = mView;
        mModel = new MainModel(this);
    }

    public void getLeftData(){
        mModel.getPersonalName();
        mModel.getPersonalIcon();
        mModel.getLeftGroupData();
    }

    @Override
    public void updateGroupView(List<GroupInfo> list) {
        mView.updateGroupView(list);
    }

    @Override
    public void updatePersonalName(String name) {
        mView.updatePersonalName(name);
    }

    @Override
    public void updatePersonalIcon(String path) {
        mView.updatePersonalIcon(path);
    }
}
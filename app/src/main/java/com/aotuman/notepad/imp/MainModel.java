package com.aotuman.notepad.imp;

import com.aotuman.notepad.define.IMainModel;
import com.aotuman.notepad.define.IMainPresenter;
import com.aotuman.notepad.entry.GroupInfo;

import java.util.ArrayList;
import java.util.List;

public class MainModel implements IMainModel {

    private IMainPresenter presenter;

    public MainModel(IMainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getLeftGroupData() {
        List<GroupInfo> list = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            list.add(new GroupInfo("分组"+i,i));
        }
        presenter.updateGroupView(list);
    }

    @Override
    public void getPersonalName() {
        String name = "TestName";
        presenter.updatePersonalName(name);
    }

    @Override
    public void getPersonalIcon() {
        presenter.updatePersonalIcon("");
    }

}
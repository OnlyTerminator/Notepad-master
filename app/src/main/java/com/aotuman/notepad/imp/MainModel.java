package com.aotuman.notepad.imp;

import android.text.TextUtils;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.define.IMainModel;
import com.aotuman.notepad.define.IMainPresenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        NoteGroupDataManager manager = NoteGroupDataManager.getInstance(ATMApplication.getInstance());
        List<GroupInfo> groupInfos = manager.findAllNotepad();
        if(null == groupInfos || groupInfos.isEmpty()) {
//            list.add(new GroupInfo("全部",0));
            list.add(new GroupInfo("未分组",0));
            list.add(new GroupInfo("生活",0));
            list.add(new GroupInfo("工作",0));
            manager.initGroupInfos(list);
        }else {
            list.addAll(groupInfos);
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
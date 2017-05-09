package com.aotuman.notepad.imp;

import android.text.TextUtils;

import com.aotuman.notepad.define.IMainModel;
import com.aotuman.notepad.define.IMainPresenter;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;

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
        String group = (String) SPUtils.get(SharePreEvent.GROUP_INFO,"");
        if(TextUtils.isEmpty(group)) {
            list.add(new GroupInfo("全部",0));
            list.add(new GroupInfo("未分组",0));
            list.add(new GroupInfo("生活",0));
            list.add(new GroupInfo("工作",0));
            listToJson(list);
        }else {
            try {
                JSONArray jsonArray = new JSONArray(group);
                for (int i = 0; i < jsonArray.length(); i++) {
                    GroupInfo info = new GroupInfo();
                    JSONObject temp = (JSONObject) jsonArray.get(i);
                    info.groupName = temp.getString("groupName");
                    info.groupCount = temp.getInt("groupCount");
                    list.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

    private void listToJson(List<GroupInfo> list) {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject tmpObj = null;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                tmpObj = new JSONObject();
                tmpObj.put("groupName", list.get(i).groupName);
                tmpObj.put("groupCount", list.get(i).groupCount);
                jsonArray.put(tmpObj);
                tmpObj = null;
            }
            String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
//            jsonObject.put("groupinfos", personInfos);   // 获得JSONObject的String
            SPUtils.put(SharePreEvent.GROUP_INFO,personInfos);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
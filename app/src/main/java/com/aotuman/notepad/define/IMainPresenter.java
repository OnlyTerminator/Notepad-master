package com.aotuman.notepad.define;


import com.aotuman.notepad.base.entry.GroupInfo;

import java.util.List;

public interface IMainPresenter {
    void updateGroupView(List<GroupInfo> list);

    void updatePersonalName(String name);

    void updatePersonalIcon(String path);
}
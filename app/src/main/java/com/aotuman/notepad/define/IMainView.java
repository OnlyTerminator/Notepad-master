package com.aotuman.notepad.define;

import com.aotuman.notepad.entry.GroupInfo;

import java.util.List;

public interface IMainView {
    void updateGroupView(List<GroupInfo> list);

    void updatePersonalName(String name);

    void updatePersonalIcon(String path);
}
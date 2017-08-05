package com.aotuman.notepad.base.entry;

import java.io.Serializable;

/**
 * Created by aotuman on 2017/5/4.
 */

public class GroupInfo implements Serializable {
    private static final long serialVersionUID = 7247714666080613254L;

    public String groupName;
    public int groupCount;

    public GroupInfo(){}

    public GroupInfo(String name,int count){
        groupName = name;
        groupCount = count;
    }
}

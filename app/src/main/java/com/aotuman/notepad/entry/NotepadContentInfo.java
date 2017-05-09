package com.aotuman.notepad.entry;

import java.io.Serializable;

/**
 * Created by 凹凸曼 on 2017/5/7.
 */

public class NotepadContentInfo implements Serializable{
    private static final long serialVersionUID = 0;

    public String title;
    public String content;
    public String time;
    public String iconPath;

    public NotepadContentInfo(String title,String content){
        this.content = content;
        this.title = title;
    }
}

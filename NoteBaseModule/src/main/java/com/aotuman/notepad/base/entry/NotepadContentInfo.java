package com.aotuman.notepad.base.entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 凹凸曼 on 2017/5/7.
 */

public class NotepadContentInfo implements Serializable {
    private static final long serialVersionUID = 0;

    public int id;
    public String title;
    public String content;
    public String time;
    public String group;
    public String imageLists;

    public NotepadContentInfo() {

    }

    public NotepadContentInfo(String title, String content) {
        this.content = content;
        this.title = title;
    }
}

package com.aotuman.notepad.base.entry;

import java.io.Serializable;

/**
 * Created by aotuman on 2017/5/4.
 */

public class PasswordInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public String title;
    public String name;
    public String password;
    public String groups;

    public PasswordInfo() {
    }

    public PasswordInfo(String title, String name, String password, String groups) {
        this.title = title;
        this.name = name;
        this.password = password;
        this.groups = groups;
    }
}

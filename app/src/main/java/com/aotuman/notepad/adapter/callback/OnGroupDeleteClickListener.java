package com.aotuman.notepad.adapter.callback;


import android.view.View;

import com.aotuman.notepad.base.entry.GroupInfo;

/**
 * Created by aotuman on 2017/5/16.
 */

public interface OnGroupDeleteClickListener {
    void onClick(View view, GroupInfo groupInfo, int position);
}

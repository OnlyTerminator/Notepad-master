package com.aotuman.notepad.adapter.callback;


import android.view.View;

import com.aotuman.notepad.base.entry.GroupInfo;

/**
 * Created by 凹凸曼 on 2017/5/4.
 */

public interface OnGroupEditClickListener {
    void onClick(View view, GroupInfo groupInfo, int position);
}

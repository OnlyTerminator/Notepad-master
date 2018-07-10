package com.aotuman.notepad.adapter.callback;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void onClick(View view, T t);
}

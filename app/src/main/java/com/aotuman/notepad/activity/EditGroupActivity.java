package com.aotuman.notepad.activity;

import android.app.Activity;
import android.os.Bundle;

import com.aotuman.notepad.R;
import com.aotuman.notepad.define.IEditGroupView;
import com.aotuman.notepad.imp.EditGroupPresenter;

public class EditGroupActivity extends Activity implements IEditGroupView {

    private EditGroupPresenter presenter;

    public EditGroupActivity() {
        presenter = new EditGroupPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
    }
}

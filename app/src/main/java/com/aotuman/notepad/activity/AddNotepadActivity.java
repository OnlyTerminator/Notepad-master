package com.aotuman.notepad.activity;

import android.app.Activity;
import android.os.Bundle;

import com.aotuman.notepad.R;
import com.aotuman.notepad.imp.AddNotepadPresenter;
import com.aotuman.notepad.define.IAddNotepadView;


public class AddNotepadActivity extends Activity implements IAddNotepadView {

    private AddNotepadPresenter presenter;

    public AddNotepadActivity() {
        presenter = new AddNotepadPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notepad);
    }
}

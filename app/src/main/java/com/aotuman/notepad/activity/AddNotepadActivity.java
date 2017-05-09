package com.aotuman.notepad.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.entry.NotepadContentInfo;
import com.aotuman.notepad.imp.AddNotepadPresenter;
import com.aotuman.notepad.define.IAddNotepadView;


public class AddNotepadActivity extends Activity implements IAddNotepadView {

    private AddNotepadPresenter mPresenter;
    private EditText mEditTitle;
    private EditText mEditContent;
    private TextView mTextGroup;
    private TextView mTextTime;
    public AddNotepadActivity() {
        mPresenter = new AddNotepadPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notepad);
        initView();
        initData();
    }

    private void initView(){
        mEditTitle = (EditText) findViewById(R.id.et_add_title);
        mEditContent = (EditText) findViewById(R.id.et_add_content);
        mTextGroup = (TextView) findViewById(R.id.tv_add_group);
        mTextTime = (TextView) findViewById(R.id.tv_add_time);
    }

    private void initData(){
        mPresenter.initData(getIntent());
    }

    @Override
    public void createAddNotepadView(NotepadContentInfo info) {
        if(null != info){
            String content = info.content;
            String string = info.time;
            String title = info.title;

            mEditContent.setText(content);
            mEditTitle.setText(title);
        }
    }
}

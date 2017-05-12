package com.aotuman.notepad.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.EditNotepadGroupAdapter;
import com.aotuman.notepad.adapter.MainContentAdapter;
import com.aotuman.notepad.define.IEditGroupView;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.fragment.MainFragment;
import com.aotuman.notepad.imp.EditGroupPresenter;

import java.util.ArrayList;
import java.util.List;

public class EditGroupActivity extends Activity implements IEditGroupView {

    private EditGroupPresenter presenter;
    private RecyclerView mRecycleView;
    private EditNotepadGroupAdapter mAdapter;
    private List<GroupInfo> mGroupInfos = new ArrayList<>();
    public EditGroupActivity() {
        presenter = new EditGroupPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mRecycleView = (RecyclerView) findViewById(R.id.rl_edit_group);
    }

    private void initEvent(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EditNotepadGroupAdapter(mGroupInfos, this);
        mRecycleView.setAdapter(mAdapter);
    }

    private void initData(){
        presenter.getGroupInfo();
    }

    @Override
    public void updateGroupView(List<GroupInfo> list) {
        mGroupInfos.clear();
        mGroupInfos.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}

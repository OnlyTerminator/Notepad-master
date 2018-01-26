package com.aotuman.notepad.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.EditNotepadGroupAdapter;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.presenter.EditGroupPresenter;

import java.util.ArrayList;
import java.util.List;

public class EditGroupActivity extends AppCompatActivity implements EditGroupPresenter.EditCallback {

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
        initActionBar();
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.rl_edit_group);
    }

    private void initEvent() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new EditNotepadGroupAdapter(mGroupInfos, this);
        mAdapter.setOnDeleteGroupClickListener(new OnGroupDeleteClickListener() {
            @Override
            public void onClick(View view, GroupInfo groupInfo, int position) {
                presenter.deleteNoteGroup(view, groupInfo);
            }
        });
        mAdapter.setOnEditGroupClickListener(new OnGroupEditClickListener() {
            @Override
            public void onClick(View view, GroupInfo groupInfo, int position) {
                if (position < 3) {
                    Toast.makeText(EditGroupActivity.this, "默认分组不可以修改喔！", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.addNoteGroup(EditGroupActivity.this, groupInfo.groupName);
                }
            }
        });
        mRecycleView.setAdapter(mAdapter);
    }

    private void initData() {
        presenter.getGroupInfo();
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle(R.string.edit_group_title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editgroup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(1);
                finish();
                return true;
            case R.id.action_compose:
                presenter.addNoteGroup(this, "");
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateGroupView(List<GroupInfo> list) {
        mGroupInfos.clear();
        mGroupInfos.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }
}

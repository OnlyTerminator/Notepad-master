package com.aotuman.notepad.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.EditNotepadGroupAdapter;
import com.aotuman.notepad.database.NoteGroupDataManager;
import com.aotuman.notepad.define.IEditGroupView;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.imp.EditGroupPresenter;

import java.util.ArrayList;
import java.util.List;

public class EditGroupActivity extends AppCompatActivity implements IEditGroupView {

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
                finish();
                return true;
            case R.id.action_compose:
                showInputDialog();
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

    private void showInputDialog() {
        final EditText editText = new EditText(EditGroupActivity.this);
        editText.setHint("新建分组");
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(EditGroupActivity.this);
        inputDialog.setTitle("分组").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String group = editText.getText().toString();
                        if(!TextUtils.isEmpty(group)){
                            NoteGroupDataManager.getInstance(ATMApplication.getInstance()).insertGroupInfo(new GroupInfo(group,0));
                            initData();
                        }
                    }
                }).show();
    }
}

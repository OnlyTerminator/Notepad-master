package com.aotuman.notepad.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.PasswordContentAdapter;
import com.aotuman.notepad.base.entry.PasswordInfo;
import com.aotuman.notepad.presenter.PasswordPresenter;

import java.util.ArrayList;
import java.util.List;

public class PassWordActivity extends AppCompatActivity implements PasswordPresenter.NotePasswordCallback,View.OnClickListener {
    private RecyclerView mRecycleView;
    private TextView mTvTitle;
    private PasswordContentAdapter mAdapter;
    private FloatingActionButton mAddButton;
    private PasswordPresenter mPresenter;
    private List<PasswordInfo> mListInfo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);
        initView();
        initAction();
        initData();
    }

    private void initData() {
        mPresenter = new PasswordPresenter(this);
        Intent intent = getIntent();
        if(null != intent){
            String groups = intent.getStringExtra("groups");
            if(!TextUtils.isEmpty(groups)){
                mTvTitle.setVisibility(View.VISIBLE);
                mTvTitle.setText(groups);
                mTvTitle.setHint("");
                mPresenter.initGroupInfo(groups);
            }
        }
    }

    private void initAction() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle(R.string.password_note);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.rl_password);
        mTvTitle = (TextView) findViewById(R.id.tv_pass_title);
        mAddButton = (FloatingActionButton) findViewById(R.id.btn_add);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAddButton.setOnClickListener(this);
        mAdapter = new PasswordContentAdapter(mListInfo,this);
        mRecycleView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void passwordInfos(List<PasswordInfo> infos) {
        mListInfo.clear();
        mListInfo.addAll(infos);
        if(null != mAdapter){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showpassGroup(String groups) {
        mTvTitle.setText(groups);
        mTvTitle.setHint("");
    }

    @Override
    public void addpasswordInfo(PasswordInfo info) {
        mListInfo.add(info);
        if(null != mAdapter){
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            String groups = mTvTitle.getText().toString().trim();
            if(!TextUtils.isEmpty(groups)) {
                mPresenter.showPasswordInfoDIalog(this,groups);
            }else {
                Toast.makeText(this,"请先给密码本增加分组",Toast.LENGTH_SHORT).show();
                mPresenter.showGroupInfoDIalog(this);
            }
        }
    }
}

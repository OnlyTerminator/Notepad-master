package com.aotuman.notepad.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.EditNotepadGroupAdapter;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.define.IEditGroupView;
import com.aotuman.notepad.imp.EditGroupPresenter;
import com.aotuman.notepad.view.GroupInfoDialog;

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
                showSnackbar(view, groupInfo);
            }
        });
        mAdapter.setOnEditGroupClickListener(new OnGroupEditClickListener() {
            @Override
            public void onClick(View view, GroupInfo groupInfo, int position) {
                if(position < 3){
                    Toast.makeText(EditGroupActivity.this,"默认分组不可以修改喔！",Toast.LENGTH_SHORT).show();
                }else {
                    showInputDialog(groupInfo.groupName);
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
                showInputDialog("");
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

    private void showInputDialog(final String content) {
        GroupInfoDialog.Builder builder = new GroupInfoDialog.Builder(EditGroupActivity.this);
        builder.setContent(content)
                .setPositiveButton(new GroupInfoDialog.PositiveOnClickListener() {
                    @Override
                    public void onClick(String group) {
                        if (TextUtils.isEmpty(content) && !TextUtils.isEmpty(group)) {
                            NoteGroupDataManager.getInstance(ATMApplication.getInstance()).insertGroupInfo(new GroupInfo(group, 0));
                            initData();
                        } else if (!TextUtils.isEmpty(content)) {
                            if (TextUtils.isEmpty(group)) {
                                Toast.makeText(ATMApplication.getInstance(), "修改失败，分组名不可为空！", Toast.LENGTH_SHORT).show();
                            } else if (!content.equals(group)) {
                                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupName(group, content);
                                initData();
                            }
                        }
                    }
                }).creat().show();
    }

    private void showSnackbar(View view, final GroupInfo groupInfo) {
        Snackbar snackbar = Snackbar.make(view, "主人，您真的不需要我了吗？", Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).deleteNotepadInfo(groupInfo.groupName);
                initData();
            }
        });
        snackbar.setActionTextColor(0xffffffff);
        setSnackbarColor(snackbar, 0xffffffff, 0xfff44336);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar
     * @param backgroundColor
     */
    private void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();//获取Snackbar的view
        if (view != null) {
            view.setBackgroundColor(backgroundColor);//修改view的背景色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);//获取Snackbar的message控件，修改字体颜色
        }
    }
}

package com.aotuman.notepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.MainActivity;
import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.AddNotepadAdapter;
import com.aotuman.notepad.adapter.EditNotepadGroupAdapter;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
import com.aotuman.notepad.database.NoteGroupDataManager;
import com.aotuman.notepad.database.NotepadDataManager;
import com.aotuman.notepad.define.IAddNotepadView;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.entry.NotepadContentInfo;
import com.aotuman.notepad.imp.AddNotepadPresenter;
import com.aotuman.notepad.utils.FileTool;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;
import com.aotuman.notepad.utils.TimeUtils;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class AddNotepadActivity extends AppCompatActivity implements IAddNotepadView {
    private static final String TAG = "AddNotepadActivity";
    private static final int REQUEST_CODE = 0;
    private AddNotepadPresenter mPresenter;
    private EditText mEditTitle;
    private EditText mEditContent;
    private TextView mTextGroup;
    private TextView mTextTime;
    private NotepadContentInfo mNotepad;
    private GroupInfo mGroupInfo = new GroupInfo();
    private long currentTime = System.currentTimeMillis();
    private NotepadDataManager mNotepadDataManager;
    private RecyclerView mRecyclerView;
    private AddNotepadAdapter mAdapter;
    public AddNotepadActivity() {
        mPresenter = new AddNotepadPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notepad);
        initActionBar();
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mEditTitle = (EditText) findViewById(R.id.et_add_title);
        mEditContent = (EditText) findViewById(R.id.et_add_content);
        mTextGroup = (TextView) findViewById(R.id.tv_add_group);
        mTextTime = (TextView) findViewById(R.id.tv_add_time);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_add_image);
    }

    private void initEvent(){
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()== KeyEvent.KEYCODE_ENTER);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AddNotepadAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle("编辑便签");
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_motepad, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initData(){
        mNotepadDataManager = NotepadDataManager.getInstance(ATMApplication.getInstance());
        String group = (String) SPUtils.get(SharePreEvent.GROUP_SELECTED_INFO,"");
        try {
            JSONObject jsonObject = new JSONObject(group);
            mGroupInfo.groupName = jsonObject.getString("groupName");
            mGroupInfo.groupCount = jsonObject.getInt("groupCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPresenter.initData(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(1);
                finish();
                return true;
            case R.id.action_pic:
//                Toast.makeText(this,"add pic success",Toast.LENGTH_SHORT).show();
                //限数量的多选(比喻最多9张)
                ImageSelectorUtils.openPhoto(AddNotepadActivity.this, REQUEST_CODE, false, 9);
                return true;
            case R.id.action_voice:
                Toast.makeText(this,"add voice success",Toast.LENGTH_SHORT).show();
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createAddNotepadView(NotepadContentInfo info) {
        if(null != info){
            mNotepad = info;
            String content = info.content;
            String time = info.time;
            String title = info.title;
            String group = info.group;
            mEditContent.setText(content);
            mEditTitle.setText(title);
            //设置光标位置到已有文字的后面
            if(!TextUtils.isEmpty(title)) {
                mEditTitle.setSelection(title.length());
            }
            mTextGroup.setText(group);
            mTextTime.setText(TimeUtils.timeStampToHour(Long.parseLong(time)));
        }else {
            mTextTime.setText(TimeUtils.timeStampToHour(currentTime));
            mTextGroup.setText(mGroupInfo.groupName);
        }
    }

    @Override
    public void onBackPressed(){
        Log.i(TAG, "onBackPressed");
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();
        if(null != mNotepad) {
            if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
                mNotepadDataManager.deleteNotepadInfo(mNotepad.id);
                setResult(1);
            }else {
                if ((TextUtils.isEmpty(mNotepad.title) && !TextUtils.isEmpty(title)) || !mNotepad.title.equals(title)) {
                    if ((TextUtils.isEmpty(mNotepad.content) && !TextUtils.isEmpty(content)) || !mNotepad.content.equals(content)) {
                        mNotepadDataManager.updateNotepadTitleAndContent(mNotepad.id, title, content, currentTime);
                    } else {
                        mNotepadDataManager.updateNotepadTitle(mNotepad.id, title, currentTime);
                    }
                    setResult(1);
                } else if ((TextUtils.isEmpty(mNotepad.content) && !TextUtils.isEmpty(content)) || !mNotepad.content.equals(content)) {
                    mNotepadDataManager.updateNotepadContent(mNotepad.id, content, currentTime);
                    setResult(1);
                }
            }

        }else {
            if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
                Log.i(TAG, "onPause: not add notepad");
            }else {
                mNotepad = new NotepadContentInfo();
                mNotepad.title = mEditTitle.getText().toString();
                mNotepad.content = mEditContent.getText().toString();
                mNotepad.group = mTextGroup.getText().toString();
                mNotepad.time = String.valueOf(currentTime);
                mNotepadDataManager.insertNotepadInfo(mNotepad);
                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupInfo(mGroupInfo.groupName,++mGroupInfo.groupCount);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("groupName",mGroupInfo.groupName);
                    jsonObject.put("groupCount",mGroupInfo.groupCount);
                    SPUtils.put(SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(1);
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if(null != images && !images.isEmpty()){
                for (int i = 0; i < images.size(); i++){
                    FileTool.copyFile(images.get(i), FileTool.getImageSDCardPath()+File.separator+i+".jpg");
                }
            }
        }
    }
}

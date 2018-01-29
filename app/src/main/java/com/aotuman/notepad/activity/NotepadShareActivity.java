package com.aotuman.notepad.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.utils.TimeUtils;
import com.aotuman.notepad.presenter.NotepadSharePresenter;
import com.aotuman.share.CommonUtils;
import com.aotuman.share.MJThirdShareManager;
import com.aotuman.share.entity.ShareContentConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by aotuman on 2018/1/27.
 */

public class NotepadShareActivity extends AppCompatActivity implements NotepadSharePresenter.ShareBack,View.OnClickListener{
    private NotepadSharePresenter mPresenter;
    private TextView mTVName,mTVGroup,mTVContent,mTVTime;
    private LinearLayout mLLShareContent,mLLShareBase;
    private MJThirdShareManager mMjThirdShareManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notepad_share);
        mPresenter = new NotepadSharePresenter(this);
        initActionBar();
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_notepad, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_compose:
                String title = mTVName.getText().toString();
                String content = mTVContent.getText().toString();
                final String path = CommonUtils.getFilesDir(this, "share").getAbsolutePath() + File.separator + "ATMShareImage.png";
                ShareContentConfig.Builder builder = new ShareContentConfig.Builder(title,content);
                builder.localImagePath(path);
                mMjThirdShareManager.doShare(builder.build(),true);
                mPresenter.prepareShareBitmap(mLLShareBase,path);
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mMjThirdShareManager = new MJThirdShareManager(this,null);
        mPresenter.initData(getIntent());
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle(R.string.share_notepad);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        mTVName = (TextView) findViewById(R.id.tv_share_title);
        mTVContent = (TextView) findViewById(R.id.tv_share_content);
        mTVGroup = (TextView) findViewById(R.id.tv_share_group);
        mTVTime = (TextView) findViewById(R.id.tv_share_time);
        mLLShareContent = (LinearLayout) findViewById(R.id.ll_share);
        mLLShareBase = (LinearLayout) findViewById(R.id.ll_share_base);
    }

    @Override
    public void showShareView(NotepadContentInfo info) {
        String title = TextUtils.isEmpty(info.title) ? "简易便签标题" : info.title;
        String content = TextUtils.isEmpty(info.title) ? "我是一篇没有文字内容的便签哦！" : info.content;
        mTVName.setText(title);
        mTVTime.setText(TimeUtils.timeStampToHour(Long.parseLong(info.time)));
        mTVGroup.setText(info.group);
        mTVContent.setText(content);
        if(!TextUtils.isEmpty(info.imageLists)) {
            List<String> list = new Gson().fromJson(info.imageLists, new TypeToken<List<String>>() {
            }.getType());
            for (String iPath : list){
                View view = LayoutInflater.from(this).inflate(R.layout.item_add_notepad_image,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_add_image);
                Picasso.with(this)
                        .load("file://" + iPath)
                        .centerInside()
                        .fit()
                        .noFade()
                        .noPlaceholder()
                        .into(imageView);
                mLLShareContent.addView(view);
            }
        }
    }

    @Override
    public void sharePrepareResult(boolean flag) {
        mMjThirdShareManager.prepareSuccess(flag);
    }

    @Override
    public void onClick(View v) {

    }
}

package com.aotuman.notepad.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.activity.EditGroupActivity;
import com.aotuman.notepad.adapter.NotepadGroupAdapter;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.presenter.LeftPresenter;
import com.aotuman.share.MJThirdShareManager;
import com.aotuman.share.entity.ShareChannelType;
import com.aotuman.share.entity.ShareContentConfig;
import com.aotuman.share.entity.ShareContentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/3.
 */

public class LeftFragment extends Fragment implements LeftPresenter.LeftCallback,View.OnClickListener,OnGroupClickListener{
    private static final String TAG = "LeftFragment";
    private View mView;
    private ImageView mHeadImageView;
    private TextView mNickView;
    private TextView mEditorView;
    private RecyclerView mRecycleView;
    private NotepadGroupAdapter mAdapter;
    private List<GroupInfo> mGroupInfoList = new ArrayList<>();
    private LeftPresenter mMainPresenter;
    private OnGroupClickListener mOnGroupClickListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_left, container, false);
            initView(mView);
            initEvent();
            mMainPresenter.initLeftData();
        }
        return mView;
    }

    private void initView(View view){
        mMainPresenter = new LeftPresenter(this);
        mHeadImageView = (ImageView) view.findViewById(R.id.iv_left_head);
        mNickView = (TextView) view.findViewById(R.id.tv_left_name);
        mEditorView = (TextView) view.findViewById(R.id.tv_left_editor);
        mRecycleView = (RecyclerView) view.findViewById(R.id.left_recyclerview);
    }

    private void initEvent(){
        mHeadImageView.setOnClickListener(this);
        mNickView.setOnClickListener(this);
        mEditorView.setOnClickListener(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NotepadGroupAdapter(mGroupInfoList, LeftFragment.this.getActivity());
        mAdapter.setOnGroupClickListener(this);
        mRecycleView.setAdapter(mAdapter);
    }

    public void initData(){
        mMainPresenter.initLeftData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_left_editor:
                startActivityForResult(new Intent(this.getActivity(), EditGroupActivity.class),0);
                break;
            case R.id.tv_left_name:
            case R.id.iv_left_head:
                ShareContentConfig shareContentConfig = new ShareContentConfig.Builder("超好用的笔记软件","一键添加笔记，支持桌面插件查看，方便快捷")
                        .shareUrl("http://sj.qq.com/myapp/detail.htm?apkName=com.aotuman.notepad")
                        .putShareType(ShareChannelType.QQ, ShareContentType.WEBPAGE)
                        .putShareType(ShareChannelType.WX_TIMELINE,ShareContentType.WEBPAGE)
                        .build();
                new MJThirdShareManager(this.getActivity(),null).doShare(shareContentConfig,false);
                break;
        }
    }

    @Override
    public void onClick(GroupInfo groupInfo) {
        if(null != groupInfo) {
            if(null != mOnGroupClickListener){
                mOnGroupClickListener.onClick(groupInfo);
            }
//            Toast.makeText(this.getActivity(), groupInfo.groupName, Toast.LENGTH_SHORT).show();
//            EventBus.create().sendEvent(EventBus.EventType.CHANGEGROUP,"登录成功");
        }
    }

    @Override
    public void updateGroupView(List<GroupInfo> list) {
        mGroupInfoList.clear();
        mGroupInfoList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePersonalName(String name) {
//        mNickView.setText(name);
    }

    @Override
    public void updatePersonalIcon(String path) {
        Log.i(TAG, "updatePersonalIcon: ");
    }

    public void setCityOnClickListion(OnGroupClickListener callBack){
        mOnGroupClickListener = callBack;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 1){
            mMainPresenter.initLeftData();
        }
    }
}

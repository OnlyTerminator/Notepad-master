package com.aotuman.notepad.fragment;

import android.app.Fragment;
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
import android.widget.Toast;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.NotepadGroupAdapter;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.define.IMainView;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.imp.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aotuman on 2017/5/3.
 */

public class LeftFragment extends Fragment implements IMainView,View.OnClickListener,OnGroupClickListener{
    private static final String TAG = "LeftFragment";
    private View mView;
    private ImageView mHeadImageView;
    private TextView mNickView;
    private TextView mEditorView;
    private RecyclerView mRecycleView;
    private NotepadGroupAdapter mAdapter;
    private List<GroupInfo> mGroupInfoList = new ArrayList<>();
    private MainPresenter mMainPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.fragment_left, container, false);
            initView(mView);
            initEvent();
            initData();
        }
        return mView;
    }

    private void initView(View view){
        mMainPresenter = new MainPresenter(this);
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

    private void initData(){
        mMainPresenter.getLeftData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_left_editor:
                Toast.makeText(LeftFragment.this.getActivity(),"edit group info",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_left_name:
            case R.id.iv_left_head:
                Toast.makeText(LeftFragment.this.getActivity(),"edit persion info",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onClick(GroupInfo groupInfo) {
        if(null != groupInfo) {
            Toast.makeText(this.getActivity(), groupInfo.groupName, Toast.LENGTH_SHORT).show();
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
        mNickView.setText(name);
    }

    @Override
    public void updatePersonalIcon(String path) {
        Log.i(TAG, "updatePersonalIcon: ");
    }

}

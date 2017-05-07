package com.aotuman.notepad.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.MainContentAdapter;
import com.aotuman.notepad.entry.NotepatContentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凹凸曼 on 2017/5/7.
 */

public class MainFragment extends Fragment {
    private View mView;
    private RecyclerView mRecycleView;
    private MainContentAdapter mAdapter;
    private List<NotepatContentInfo> mNotepadList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mView){
            mView = inflater.inflate(R.layout.fragment_main,container,false);
            initView(mView);
            initEvent();
            initData();
        }
        return mView;
    }

    private void initView(View view){
        mRecycleView = (RecyclerView) view.findViewById(R.id.rl_main);
    }

    private void initEvent(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainContentAdapter(mNotepadList, MainFragment.this.getActivity());
//        mAdapter.setOnGroupClickListener(this);
        mRecycleView.setAdapter(mAdapter);
    }

    private void initData(){
        String str = "";
        for (int i = 0; i < 10; i++){
            if(i%2 == 0){
                str = "都是第三方第三方水电费水电费水电费水电费水电费水电费第三方士大夫的说法都是发送到发送到发送到发送到发送到发送到";
            }else {
                str = "东方闪电撒大所大所大所大所";
            }
            NotepatContentInfo notepatContentInfo = new NotepatContentInfo(str);
            mNotepadList.add(notepatContentInfo);
        }
        mAdapter.notifyDataSetChanged();
    }
}

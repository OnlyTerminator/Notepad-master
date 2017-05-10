package com.aotuman.notepad.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.aotuman.notepad.R;
import com.aotuman.notepad.activity.AddNotepadActivity;
import com.aotuman.notepad.adapter.MainContentAdapter;
import com.aotuman.notepad.adapter.callback.OnNotepadClickListener;
import com.aotuman.notepad.database.NotepadDataManager;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.entry.NotepadContentInfo;
import com.aotuman.notepad.utils.EventBus;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;
import com.gc.materialdesign.views.ButtonFloat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by 凹凸曼 on 2017/5/7.
 */

public class MainFragment extends Fragment implements OnNotepadClickListener,View.OnClickListener{
    private View mView;
    private RecyclerView mRecycleView;
    private ButtonFloat mButton;
    private MainContentAdapter mAdapter;
    private List<NotepadContentInfo> mNotepadList = new ArrayList<>();
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
        mButton = (ButtonFloat) view.findViewById(R.id.btn_add);
    }

    private void initEvent(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainContentAdapter(mNotepadList, MainFragment.this.getActivity());
        mAdapter.setOnNotepadClickListener(this);
        mRecycleView.setAdapter(mAdapter);
        mButton.setOnClickListener(this);
    }

    public void initData(){
        mNotepadList.clear();
        String group = (String) SPUtils.get(SharePreEvent.GROUP_SELECTED_INFO,"");
        GroupInfo info = new GroupInfo("未分组",0);
        if(TextUtils.isEmpty(group)){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("groupName",info.groupName);
                jsonObject.put("groupCount",info.groupCount);
                SPUtils.put(SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JSONObject jsonObject = new JSONObject(group);
                info.groupName = jsonObject.getString("groupName");
                info.groupCount = jsonObject.getInt("groupCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<NotepadContentInfo> list = NotepadDataManager.getInstance(MainFragment.this.getActivity()).findNotepadByGroup(info.groupName);
        Collections.reverse(list); // 倒序排列
        mNotepadList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(NotepadContentInfo info) {
        Intent intent = new Intent(MainFragment.this.getActivity(),AddNotepadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("notepad", info);
        intent.putExtra("type","content");
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent,0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add){
            getActivity().startActivityForResult(new Intent(this.getActivity(), AddNotepadActivity.class),0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == 1){
//            initData();
//        }
    }

//    @Override
//    public void onHandleEvent(int eventType, Object obj) {
//        if(eventType == EventBus.EventType.CHANGEGROUP){
//            initData();
//        }
//    }
}

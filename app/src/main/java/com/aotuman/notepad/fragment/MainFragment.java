package com.aotuman.notepad.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.activity.AddNotepadActivity;
import com.aotuman.notepad.activity.CheckPasswordActivity;
import com.aotuman.notepad.adapter.MainContentAdapter;
import com.aotuman.notepad.adapter.callback.OnNotepadClickListener;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.aotuman.notepad.presenter.MainPresenter;
import com.leo.gesturelibray.enums.LockMode;
import com.leo.gesturelibray.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凹凸曼 on 2017/5/7.
 */

public class MainFragment extends Fragment implements OnNotepadClickListener,View.OnClickListener,MainPresenter.MainCallback{
    private View mView;
    private RecyclerView mRecycleView;
    private FloatingActionButton mButton;
    private MainContentAdapter mAdapter;
    private List<NotepadContentInfo> mNotepadList = new ArrayList<>();
    private MainPresenter mMainPresenter;
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
        mButton = (FloatingActionButton) view.findViewById(R.id.btn_add);
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
        mMainPresenter = new MainPresenter(this);
        mMainPresenter.getNoteList();
    }

    public void getUpdateList(){
        if(null != mMainPresenter) {
            mMainPresenter.getNoteList();
        }
    }
    @Override
    public void onClick(NotepadContentInfo info) {
        if("密码".equals(info.group)){
            Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
            String password = (String) SPUtils.get(getActivity(), SharePreEvent.CHECK_PASS_WORD, "");
            if (TextUtils.isEmpty(password)) {
                intent.putExtra("check_mode", LockMode.SETTING_PASSWORD);
            } else {
                intent.putExtra("check_mode", LockMode.VERIFY_PASSWORD);
            }
            intent.putExtra("groups",info.title);
            getActivity().startActivityForResult(intent,0);
        } else {
            Intent intent = new Intent(MainFragment.this.getActivity(), AddNotepadActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("notepad", info);
            intent.putExtra("type", "content");
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            boolean isPass = false;
            String group = (String) SPUtils.get(ATMApplication.getInstance(), SharePreEvent.GROUP_SELECTED_INFO, "");
            try {
                JSONObject jsonObject = new JSONObject(group);
                isPass = "密码".equals(jsonObject.getString("groupName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isPass) {
                Intent intent = new Intent(getActivity(), CheckPasswordActivity.class);
                String password = (String) SPUtils.get(getActivity(), SharePreEvent.CHECK_PASS_WORD, "");
                if (TextUtils.isEmpty(password)) {
                    intent.putExtra("check_mode", LockMode.SETTING_PASSWORD);
                } else {
                    intent.putExtra("check_mode", LockMode.VERIFY_PASSWORD);
                }
                getActivity().startActivityForResult(intent,0);
            } else {
                getActivity().startActivityForResult(new Intent(this.getActivity(), AddNotepadActivity.class), 0);
            }
        }
    }

    @Override
    public void updateNoteList(List<NotepadContentInfo> list) {
        mNotepadList.clear();
        mNotepadList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}

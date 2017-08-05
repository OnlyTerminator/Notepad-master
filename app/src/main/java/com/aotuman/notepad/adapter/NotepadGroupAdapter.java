package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class NotepadGroupAdapter extends RecyclerView.Adapter<NotepadGroupAdapter.MyViewHolder> {
    private List<GroupInfo> mGroupInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnGroupClickListener mOnGroupClickListener;
    private View mSelectedView;
    public NotepadGroupAdapter(List<GroupInfo> groupInfoList, Context context) {
        this.mGroupInfoList = groupInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_left_group, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupInfo groupInfo = mGroupInfoList.get(position);
        int count = (int) SPUtils.get(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_POSITION,0);
        if (position == count){
            mSelectedView = holder.rl_lrft_group;
            holder.rl_lrft_group.setBackgroundResource(R.color.groupSelect);
        }
        if(null != groupInfo) {
            holder.tv_group_name.setText(groupInfo.groupName);
            holder.tv_group_count.setText(String.valueOf(groupInfo.groupCount));
            holder.rl_lrft_group.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return null == mGroupInfoList ? 0 : mGroupInfoList.size();
    }


    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener){
        this.mOnGroupClickListener = onGroupClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_group_name;
        private TextView tv_group_count;
        private RelativeLayout rl_lrft_group;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
            tv_group_count = (TextView) itemView.findViewById(R.id.tv_group_number);
            rl_lrft_group = (RelativeLayout) itemView.findViewById(R.id.rl_left_group);
            rl_lrft_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mOnGroupClickListener){
                        Object obj = rl_lrft_group.getTag();
                        int position = null == obj ? 0 : (int) obj;
                        GroupInfo groupInfo = mGroupInfoList.get(position);
                        SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_POSITION,position);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("groupName",groupInfo.groupName);
                            jsonObject.put("groupCount",groupInfo.groupCount);
                            SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mOnGroupClickListener.onClick(groupInfo);
                        if(null != mSelectedView){
                            mSelectedView.setBackgroundResource(R.color.groupNoSelect);
                            rl_lrft_group.setBackgroundResource(R.color.groupSelect);
                            mSelectedView = rl_lrft_group;
                        }
                    }
                }
            });
        }
    }
}

package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.entry.GroupInfo;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class NotepadGroupAdapter extends RecyclerView.Adapter<NotepadGroupAdapter.MyViewHolder> {
    private List<GroupInfo> mGroupInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

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
        if(null != groupInfo) {
            holder.tv_group_name.setText(groupInfo.groupName);
            holder.tv_group_count.setText(String.valueOf(groupInfo.groupCount));
        }
    }

    @Override
    public int getItemCount() {
        return null == mGroupInfoList ? 0 : mGroupInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_group_name;
        private TextView tv_group_count;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
            tv_group_count = (TextView) itemView.findViewById(R.id.tv_group_number);
        }
    }
}

package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;

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
        int count = (int) SPUtils.get(mContext, SharePreEvent.GROUP_SELECTED,0);
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
                        SPUtils.put(mContext, SharePreEvent.GROUP_SELECTED,position);
                        mOnGroupClickListener.onClick(mGroupInfoList.get(position));
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

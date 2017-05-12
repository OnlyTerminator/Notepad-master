package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class EditNotepadGroupAdapter extends RecyclerView.Adapter<EditNotepadGroupAdapter.MyViewHolder> {
    private List<GroupInfo> mGroupInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnGroupClickListener mOnGroupClickListener;
    private View mSelectedView;
    public EditNotepadGroupAdapter(List<GroupInfo> groupInfoList, Context context) {
        this.mGroupInfoList = groupInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_edit_group, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupInfo groupInfo = mGroupInfoList.get(position);
        if(null != groupInfo) {
            holder.tv_group_name.setText(groupInfo.groupName);
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
        private RadioButton rb_group_select;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
            rb_group_select = (RadioButton) itemView.findViewById(R.id.rb_group_select);

        }
    }
}

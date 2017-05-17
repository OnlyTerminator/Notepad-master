package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
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
    private OnGroupDeleteClickListener mOnDeleteGroupClickListener;
    private OnGroupEditClickListener mOnEditGroupClickListener;
    private int mBackground;
    public EditNotepadGroupAdapter(List<GroupInfo> groupInfoList, Context context) {
        this.mGroupInfoList = groupInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        this.mBackground = typedValue.resourceId;
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
        if(position < 3){
            holder.iv_group_delete.setVisibility(View.INVISIBLE);
            holder.itemView.setBackgroundResource(R.color.appWhite);
        }
        if(null != groupInfo) {
            holder.tv_group_name.setText(groupInfo.groupName);
            holder.iv_group_edit.setTag(position);
            holder.iv_group_delete.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return null == mGroupInfoList ? 0 : mGroupInfoList.size();
    }


    public void setOnDeleteGroupClickListener(OnGroupDeleteClickListener onGroupClickListener){
        this.mOnDeleteGroupClickListener = onGroupClickListener;
    }

    public void setOnEditGroupClickListener(OnGroupEditClickListener onGroupClickListener){
        this.mOnEditGroupClickListener = onGroupClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_group_name;
        private ImageView iv_group_edit;
        private ImageView iv_group_delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_group_name = (TextView) itemView.findViewById(R.id.tv_group_name);
            iv_group_edit = (ImageView) itemView.findViewById(R.id.iv_group_edit);
            iv_group_delete = (ImageView) itemView.findViewById(R.id.iv_group_delete);
            iv_group_delete.setBackgroundResource(mBackground);
            iv_group_edit.setBackgroundResource(mBackground);
            iv_group_delete.setOnClickListener(this);
            iv_group_edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_group_edit:
                    int pose = (int) iv_group_edit.getTag();
                    GroupInfo groupInfo = mGroupInfoList.get(pose);
                    if(null != mOnEditGroupClickListener){
                        mOnEditGroupClickListener.onClick(itemView,groupInfo,pose);
                    }
                    break;
                case R.id.iv_group_delete:
                    int posd = (int) iv_group_delete.getTag();
                    GroupInfo deleteGroup = mGroupInfoList.get(posd);
                    if(null != mOnDeleteGroupClickListener){
                        mOnDeleteGroupClickListener.onClick(itemView,deleteGroup,posd);
                    }
                    break;
            }
        }
    }
}

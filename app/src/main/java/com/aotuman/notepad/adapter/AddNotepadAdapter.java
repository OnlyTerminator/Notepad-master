package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
import com.aotuman.notepad.entry.GroupInfo;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class AddNotepadAdapter extends RecyclerView.Adapter<AddNotepadAdapter.MyViewHolder> {
    private List<GroupInfo> mGroupInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnGroupDeleteClickListener mOnDeleteGroupClickListener;
    private OnGroupEditClickListener mOnEditGroupClickListener;
    private int mBackground;

    public AddNotepadAdapter(List<GroupInfo> groupInfoList, Context context) {
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
        View view = mLayoutInflater.inflate(R.layout.item_add_notepad_image, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return null == mGroupInfoList ? 0 : mGroupInfoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_add_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_add_image = (ImageView) itemView.findViewById(R.id.iv_add_image);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }
}

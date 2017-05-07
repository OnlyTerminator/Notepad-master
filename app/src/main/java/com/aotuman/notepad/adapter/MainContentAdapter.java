package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.entry.GroupInfo;
import com.aotuman.notepad.entry.NotepatContentInfo;
import com.aotuman.notepad.utils.SPUtils;
import com.aotuman.notepad.utils.SharePreEvent;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class MainContentAdapter extends RecyclerView.Adapter<MainContentAdapter.MyViewHolder> {
    private List<NotepatContentInfo> mContentInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View mSelectedView;
    public MainContentAdapter(List<NotepatContentInfo> contentInfoList, Context context) {
        this.mContentInfoList = contentInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_main_content, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotepatContentInfo notepatContentInfo = mContentInfoList.get(position);
        if(null != notepatContentInfo){
            holder.tv_content_title.setText(notepatContentInfo.content);
        }

    }

    @Override
    public int getItemCount() {
        return null == mContentInfoList ? 0 : mContentInfoList.size();
    }


    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener){
//        this.mOnGroupClickListener = onGroupClickListener;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_content_title;
        private TextView tv_content_time;
        private RelativeLayout rl_main_content;
        private ImageView iv_content_icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content_title = (TextView) itemView.findViewById(R.id.tv_content_title);
            tv_content_time = (TextView) itemView.findViewById(R.id.tv_content_time);
            rl_main_content = (RelativeLayout) itemView.findViewById(R.id.rl_main_content);
            iv_content_icon = (ImageView) itemView.findViewById(R.id.iv_content_image);
            rl_main_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}

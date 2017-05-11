package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupClickListener;
import com.aotuman.notepad.adapter.callback.OnNotepadClickListener;
import com.aotuman.notepad.entry.NotepadContentInfo;
import com.aotuman.notepad.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class MainContentAdapter extends RecyclerView.Adapter<MainContentAdapter.MyViewHolder> {
    private List<NotepadContentInfo> mContentInfoList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private View mSelectedView;
    private OnNotepadClickListener mOnListener;
    private int mBackground;
    public MainContentAdapter(List<NotepadContentInfo> contentInfoList, Context context) {
        this.mContentInfoList = contentInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        this.mBackground = typedValue.resourceId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_main_content, parent, false);
        view.setBackgroundResource(mBackground);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotepadContentInfo notepatContentInfo = mContentInfoList.get(position);
        if(null != notepatContentInfo){
            String title = notepatContentInfo.title;
            if(TextUtils.isEmpty(title)) {
                holder.tv_content_title.setText(notepatContentInfo.content);
            }else {
                holder.tv_content_title.setText(title);
            }
            holder.tv_content_time.setText(TimeUtils.timeStampToDate(Long.parseLong(notepatContentInfo.time)));
            holder.rl_main_content.setTag(notepatContentInfo);
        }

    }

    @Override
    public int getItemCount() {
        return null == mContentInfoList ? 0 : mContentInfoList.size();
    }


    public void setOnNotepadClickListener(OnNotepadClickListener listener){
        this.mOnListener = listener;
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
            rl_main_content.setBackgroundResource(mBackground);
            rl_main_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != mOnListener){
                        Object obj = rl_main_content.getTag();
                        NotepadContentInfo info = null == obj ? null : (NotepadContentInfo) obj;
                        mOnListener.onClick(info);
                    }
                }
            });
        }
    }
}

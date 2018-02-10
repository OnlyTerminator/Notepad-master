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
import com.aotuman.notepad.adapter.callback.OnNotepadClickListener;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.entry.PasswordInfo;
import com.aotuman.notepad.base.utils.TimeUtils;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class PasswordContentAdapter extends RecyclerView.Adapter<PasswordContentAdapter.MyViewHolder> {
    private List<PasswordInfo> mPasswordInfo;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnNotepadClickListener mOnListener;
    private int mBackground;
    public PasswordContentAdapter(List<PasswordInfo> contentInfoList, Context context) {
        this.mPasswordInfo = contentInfoList;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        this.mBackground = typedValue.resourceId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_password_content, parent, false);
        view.setBackgroundResource(mBackground);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PasswordInfo passwordInfo = mPasswordInfo.get(position);
        if(null != passwordInfo){
            holder.tv_pass_title.setText(passwordInfo.title);
            holder.tv_pass_name.setText(passwordInfo.name);
            holder.tv_pass_word.setText(passwordInfo.password);
        }

    }

    @Override
    public int getItemCount() {
        return null == mPasswordInfo ? 0 : mPasswordInfo.size();
    }


    public void setOnNotepadClickListener(OnNotepadClickListener listener){
        this.mOnListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_pass_title;
        private TextView tv_pass_name;
        private TextView tv_pass_word;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_pass_name = (TextView) itemView.findViewById(R.id.tv_pass_name);
            tv_pass_title = (TextView) itemView.findViewById(R.id.tv_pass_title);
            tv_pass_word = (TextView) itemView.findViewById(R.id.tv_pass_word);
        }
    }
}

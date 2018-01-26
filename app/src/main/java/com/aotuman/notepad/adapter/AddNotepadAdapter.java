package com.aotuman.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.callback.OnGroupDeleteClickListener;
import com.aotuman.notepad.adapter.callback.OnGroupEditClickListener;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aotuman on 2017/5/4.
 */

public class AddNotepadAdapter extends RecyclerView.Adapter<AddNotepadAdapter.MyViewHolder> {
    private List<String> mImageList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private NotepadImageClickListener mListener;

    public AddNotepadAdapter(List<String> images, Context context) {
        this.mImageList = images;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        View view = mLayoutInflater.inflate(R.layout.item_add_notepad_image, parent, false);
        myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public void setImageOnClickListener(NotepadImageClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (null != mImageList && position < mImageList.size()) {
            String imagePath = mImageList.get(position);
            if (!TextUtils.isEmpty(imagePath)) {
                holder.iv_add_image.setTag(position);
                Picasso.with(mContext)
                        .load("file://" + imagePath)
                        .centerInside()
                        .fit()
                        .noFade()
                        .noPlaceholder()
                        .into(holder.iv_add_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mImageList ? 0 : mImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_add_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_add_image = (ImageView) itemView.findViewById(R.id.iv_add_image);
            iv_add_image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mListener) {
                mListener.onclick((int)v.getTag());
            }
        }
    }

    public interface NotepadImageClickListener {
        void onclick(int position);
    }
}

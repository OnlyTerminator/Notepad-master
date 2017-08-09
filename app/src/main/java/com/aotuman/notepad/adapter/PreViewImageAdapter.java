package com.aotuman.notepad.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.aotuman.notepad.view.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

/**
 * Created by guodong.hou on 2017/7/14.
 */

public class PreViewImageAdapter extends PagerAdapter {
    private Object mTag = new Object();
    private Context mContext;
    private ArrayList<String> mData;
    private int mCurrentPosition;
    private Callback mPicassoCallback;

    public PreViewImageAdapter(Context context, ArrayList<String> data, int currentPosition) {
        mContext = context;
        mData = data;
        mCurrentPosition = currentPosition;
        mPicassoCallback = new Callback() {
            @Override
            public void onSuccess() {
                if (mOnLoadFinishListener != null) {
                    mOnLoadFinishListener.loadFinish();
                }
            }

            @Override
            public void onError() {
                if (mOnLoadFinishListener != null) {
                    mOnLoadFinishListener.loadFinish();
                }
            }
        };
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        TouchImageView photoView = new TouchImageView(container.getContext());
        RequestCreator creator = Picasso.with(mContext)
                .load(mData.get(position))
                .config(Bitmap.Config.RGB_565)
                .centerInside()
                .fit()
                .noFade()
                .noPlaceholder()
                .tag(mTag);
        creator.into(photoView, mCurrentPosition == position ? mPicassoCallback : null);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private OnLoadFinishListener mOnLoadFinishListener;

    public void setOnLoadFinishListener(OnLoadFinishListener listener) {
        mOnLoadFinishListener = listener;
    }

    public interface OnLoadFinishListener {
        void loadFinish();
    }

    public void onDestroy() {
        Picasso.with(mContext).cancelTag(mTag);
        mOnLoadFinishListener = null;
    }
}

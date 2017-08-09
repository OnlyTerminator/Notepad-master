package com.aotuman.notepad.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.PreViewImageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xufeng.zhang on 2017/8/8.
 */

public class PictureDetailActivity extends Activity {
    private ViewPager mViewPager;
    private PreViewImageAdapter mAdapter;
    private ArrayList<String> mImages = new ArrayList<>();
    private int mCurrentIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        mViewPager = (ViewPager) findViewById(R.id.vp_picture_detail);
    }

    private void initData(){

    }

    private void intEvent(){
        mAdapter = new PreViewImageAdapter(this,mImages,mCurrentIndex);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}

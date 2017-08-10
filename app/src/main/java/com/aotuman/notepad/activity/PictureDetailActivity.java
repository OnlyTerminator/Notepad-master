package com.aotuman.notepad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.PreViewImageAdapter;
import com.aotuman.notepad.base.utils.CommonUtils;
import com.aotuman.notepad.view.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by xufeng.zhang on 2017/8/8.
 */

public class PictureDetailActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private PreViewImageAdapter mAdapter;
    private ArrayList<String> mImages = new ArrayList<>();
    private int mCurrentIndex;
    private TouchImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        mViewPager = (ViewPager) findViewById(R.id.vp_picture_detail);
        mImageView = (TouchImageView) findViewById(R.id.iv_picture);
        initData();
        intEvent();
        initActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    private void initData(){
        Intent intent = getIntent();
        mImages.addAll(intent.getStringArrayListExtra("images"));
        mCurrentIndex = intent.getIntExtra("index",0);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle(R.string.picture_detail);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void intEvent(){
        mAdapter = new PreViewImageAdapter(this,mImages,mCurrentIndex);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Picasso.with(PictureDetailActivity.this)
                        .load("file://"+mImages.get(position))
                        .config(Bitmap.Config.RGB_565)
                        .centerInside()
                        .fit()
                        .noFade()
                        .noPlaceholder()
                        .into(mImageView);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private float downX, downY, dragPercent;
    private long downTime;
    private boolean pointerDown = false;
    /**
     * dispatchTouchEvent()方法里，同时实现了“拖拽退出”、“点击退出”两个功能
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mViewPager == null) {
            return super.dispatchTouchEvent(ev);
        }
        if (isZoomed()) {
            secondClickTime = SystemClock.uptimeMillis();
            return super.dispatchTouchEvent(ev);
        } else {
            secondClickTime = 0;
        }
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                pointerDown = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //pointerDown = false;
                break;
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (pointerDown) {
                    if (dragPercent == 0) {
                        return super.dispatchTouchEvent(ev);
                    } else {
                        return false;
                    }
                }
                float moveX = ev.getX();
                float moveY = ev.getY();
                float absDisX = Math.abs(moveX - downX);
                float absDisY = Math.abs(moveY - downY);
                if (dragPercent == 0) {
                    if (absDisX < CommonUtils.dp2px(2) && absDisX * 1.8f < absDisY) {
                        dragPercent = absDisY * 2 / CommonUtils.getScreenHeight();
                    }
                } else {
                    dragPercent = absDisY * 2 / CommonUtils.getScreenHeight();
                    drag(1 - dragPercent, moveX - downX, moveY - downY);
                }
                break;
            case MotionEvent.ACTION_UP:
//                if (shouldDispatch(ev)) {
//                    dragPercent = 0;
//                    pointerDown = false;
//                    return super.dispatchTouchEvent(ev);
//                }
                float upX = ev.getX();
                float upY = ev.getY();
                if (System.currentTimeMillis() - downTime < 300 && Math.abs(upX - downX) < CommonUtils.dp2px(8) && Math.abs(upY - downY) < CommonUtils.dp2px(8)) {
                    onCustomClick();
                } else {
                    if (dragPercent > 0.75f) {
//                        exitAnimation();
//                        drag(1.f, 0.f, 0.f);
                        finish();
                    } else {
                        drag(1.f, 0.f, 0.f);
                    }
                }
                dragPercent = 0;
                pointerDown = false;
                break;
        }
        if (dragPercent == 0) {
            return super.dispatchTouchEvent(ev);
        } else {
            return false;
        }
    }

    private boolean isZoomed() {
        int count = mViewPager.getChildCount();
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem < 0 || currentItem > count - 1) {
            return false;
        }
        View child = mViewPager.getChildAt(currentItem);
        if (child instanceof TouchImageView) {
            TouchImageView imageView = (TouchImageView) child;
            return imageView.isZoomed();
        }
        return false;
    }

    private void drag(float percent, float disX, float disY) {
        mViewPager.setTranslationX(disX);
        mViewPager.setTranslationY(disY);
        mViewPager.setScaleX(percent);
        mViewPager.setScaleY(percent);
        mImageView.setTranslationX(disX);
        mImageView.setTranslationY(disY);
        mImageView.setScaleX(percent);
        mImageView.setScaleY(percent);
    }

    long firstClickTime = 0;
    long secondClickTime = 0;

    private void onCustomClick() {
        if (firstClickTime > 0) {
            secondClickTime = SystemClock.uptimeMillis();
            //触发双击，放大（不动作）
            return;
        }
        firstClickTime = SystemClock.uptimeMillis();
//        MJPools.executeWithMJThreadPool(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(300);
//                    if (secondClickTime == 0) {
//                        mBottomView.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                exitAnimation();//触发点击，退出
//                            }
//                        });
//                    }
//                    firstClickTime = 0;
//                    secondClickTime = 0;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

//    private boolean shouldDispatch(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//        int left = mDownloadBtn.getLeft();
//        int right = mDownloadBtn.getRight();
//        int top = mDownloadBtn.getTop();
//        int bottom = mDownloadBtn.getBottom();
//        if (left <= x &&
//                x <= right &&
//                top <= y &&
//                y <= bottom) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}

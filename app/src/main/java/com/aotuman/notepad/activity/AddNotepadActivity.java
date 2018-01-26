package com.aotuman.notepad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.adapter.AddNotepadAdapter;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.database.NotepadDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.utils.FileTool;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.aotuman.notepad.base.utils.TimeUtils;
import com.aotuman.notepad.presenter.AddNotepadPresenter;
import com.aotuman.share.CommonUtils;
import com.aotuman.share.MJThirdShareManager;
import com.aotuman.share.entity.ShareContentConfig;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddNotepadActivity extends AppCompatActivity implements AddNotepadPresenter.AddNotepadCallback {
    private static final String TAG = "AddNotepadActivity";
    private static final int REQUEST_CODE = 0;
    private AddNotepadPresenter mPresenter;
    private EditText mEditTitle;
    private EditText mEditContent;
    private TextView mTextGroup;
    private TextView mTextTime;
    private LinearLayout mLinearLayout;
    private NotepadContentInfo mNotepad;
    private GroupInfo mGroupInfo = new GroupInfo();
    private long currentTime = System.currentTimeMillis();
    private NotepadDataManager mNotepadDataManager;
    private RecyclerView mRecyclerView;
    private AddNotepadAdapter mAdapter;
    private MJThirdShareManager mMjThirdShareManager;
    private ArrayList<String> mImagePath = new ArrayList<>();
    public AddNotepadActivity() {
        mPresenter = new AddNotepadPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notepad);
        initActionBar();
        initView();
        initEvent();
        initData();
    }

    private void initView(){
        mEditTitle = (EditText) findViewById(R.id.et_add_title);
        mEditContent = (EditText) findViewById(R.id.et_add_content);
        mTextGroup = (TextView) findViewById(R.id.tv_add_group);
        mTextTime = (TextView) findViewById(R.id.tv_add_time);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_add_image);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_notepad_content);
    }

    private void initEvent(){
        mMjThirdShareManager = new MJThirdShareManager(this,null);
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()== KeyEvent.KEYCODE_ENTER);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AddNotepadAdapter(mImagePath, this);
        mAdapter.setImageOnClickListener(new AddNotepadAdapter.NotepadImageClickListener() {
            @Override
            public void onclick(int position) {
                Intent intent = new Intent(AddNotepadActivity.this,PictureDetailActivity.class);
                intent.putExtra("index",position);
                intent.putStringArrayListExtra("images",mImagePath);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle("编辑便签");
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_motepad, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initData(){
        mNotepadDataManager = NotepadDataManager.getInstance(ATMApplication.getInstance());
        String group = (String) SPUtils.get(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,"");
        try {
            JSONObject jsonObject = new JSONObject(group);
            mGroupInfo.groupName = jsonObject.getString("groupName");
            mGroupInfo.groupCount = jsonObject.getInt("groupCount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mPresenter.initData(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                setResult(1);
                finish();
                return true;
            case R.id.action_pic:
//                Toast.makeText(this,"add pic success",Toast.LENGTH_SHORT).show();
                //限数量的多选(比喻最多9张)
                ImageSelectorUtils.openPhoto(AddNotepadActivity.this, REQUEST_CODE, false, 9);
                return true;
            case R.id.action_voice:
                Toast.makeText(this,"add voice success",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:
                String title = mEditTitle.getText().toString();
                String content = mEditContent.getText().toString();
                final String path = CommonUtils.getFilesDir(this, "share").getAbsolutePath() + File.separator + "ATMShareImage.png";
                ShareContentConfig.Builder builder = new ShareContentConfig.Builder(title,content);
                builder.localImagePath(path);
                mMjThirdShareManager.doShare(builder.build(),true);
                final List<Bitmap> bitmapList = new ArrayList<>();
                mEditTitle.buildDrawingCache();
                mTextGroup.buildDrawingCache();
                mEditContent.buildDrawingCache();
                Bitmap titleBitmap = mEditTitle.getDrawingCache();
                Bitmap groupBitmap = mTextGroup.getDrawingCache();
                Bitmap contentBitmap = mEditContent.getDrawingCache();
                bitmapList.add(titleBitmap);
                bitmapList.add(groupBitmap);
                bitmapList.add(contentBitmap);
                Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
//                        Bitmap last = addBitmap(bitmapList);
                        if(null != mImagePath && !mImagePath.isEmpty()){
                            for (String iPath : mImagePath){
                                if(!TextUtils.isEmpty(iPath)) {
//                                    bitmapList.clear();
//                                    bitmapList.add(last);
                                    Bitmap bitmap = Picasso.with(AddNotepadActivity.this).load("file://"+iPath).get();
                                    bitmapList.add(bitmap);
//                                    last = addBitmap(bitmapList);
                                }
                            }
                        }
                        Bitmap last = addBitmap(bitmapList);
                        CommonUtils.writeBitmap(new File(path),last,100,true);
                        emitter.onNext(new Object());
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object list) {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mMjThirdShareManager.prepareSuccess(false);
                            }

                            @Override
                            public void onComplete() {
                                mMjThirdShareManager.prepareSuccess(true);
                            }
                        });
//                List<Bitmap> bitmapList = new ArrayList<>();
//
//                int count = mRecyclerView.getAdapter().getItemCount();
//                for (int i = 0; i < count;i++){
//                    View view = mRecyclerView.getChildAt(i);
//                    view.buildDrawingCache();
//                    Bitmap bitmap2 = view.getDrawingCache();
//                    bitmapList.add(bitmap2);
//                }
//                Bitmap bitmap = addBitmap(bitmapList);
//                View view = LayoutInflater.from(this).inflate(R.layout.view_notepad_share,null);
//                LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
//                if(null != mImagePath && !mImagePath.isEmpty()){
//                    for (String iPath : mImagePath) {
//                        ImageView imageView = new ImageView(this);
//                        Picasso.with(this).load(iPath).into(imageView);
//                        ll_share.addView(imageView);
//                    }
//                }
//                ll_share.setDrawingCacheEnabled(false);
//                ll_share.setDrawingCacheEnabled(true);
//                ll_share.buildDrawingCache();
//                Bitmap bitmap = ll_share.getDrawingCache();
//                int w = ll_share.getMeasuredWidth();
//                int h = ll_share.getMeasuredHeight();
//                Bitmap bitmap = loadBitmapFromView(view,w,h,true);
//                CommonUtils.writeBitmap(new File(path),bitmap,100,true);

                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    public Bitmap loadBitmapFromView(View view, int width, int height, boolean hasMeasure) {
        if (view == null) {
            return null;
        }
        view.measure(view.getLayoutParams().width, view.getLayoutParams().height);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // 生成bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // 利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);
        // 把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }

    @Override
    public void createAddNotepadView(NotepadContentInfo info) {
        if(null != info){
            mNotepad = info;
            String content = info.content;
            String time = info.time;
            String title = info.title;
            String group = info.group;
            if(!TextUtils.isEmpty(info.imageLists)) {
                mImagePath.clear();
                List<String> list = new Gson().fromJson(info.imageLists, new TypeToken<List<String>>() {
                }.getType());
                mImagePath.addAll(list);
            }
            mEditContent.setText(content);
            mEditTitle.setText(title);
            //设置光标位置到已有文字的后面
            if(!TextUtils.isEmpty(title)) {
                mEditTitle.setSelection(title.length());
            }
            mTextGroup.setText(group);
            mTextTime.setText(TimeUtils.timeStampToHour(Long.parseLong(time)));
            mAdapter.notifyDataSetChanged();
        }else {
            mTextTime.setText(TimeUtils.timeStampToHour(currentTime));
            mTextGroup.setText(mGroupInfo.groupName);
        }
    }

    @Override
    public void onBackPressed(){
        Log.i(TAG, "onBackPressed");
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();
        if(null != mNotepad) {
            if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
                mNotepadDataManager.deleteNotepadInfo(mNotepad.id);
                setResult(1);
            }else {
                if ((TextUtils.isEmpty(mNotepad.title) && !TextUtils.isEmpty(title)) || !mNotepad.title.equals(title)) {
                    if ((TextUtils.isEmpty(mNotepad.content) && !TextUtils.isEmpty(content)) || !mNotepad.content.equals(content)) {
                        mNotepadDataManager.updateNotepadTitleAndContent(mNotepad.id, title, content, currentTime,new Gson().toJson(mImagePath));
                    } else {
                        mNotepadDataManager.updateNotepadTitle(mNotepad.id, title, currentTime,new Gson().toJson(mImagePath));
                    }
                    setResult(1);
                } else if ((TextUtils.isEmpty(mNotepad.content) && !TextUtils.isEmpty(content)) || !mNotepad.content.equals(content)) {
                    mNotepadDataManager.updateNotepadContent(mNotepad.id, content, currentTime,new Gson().toJson(mImagePath));
                    setResult(1);
                }else {
                    mNotepadDataManager.updateNotepadPic(mNotepad.id, currentTime,new Gson().toJson(mImagePath));
                    setResult(1);
                }
            }

        }else {
            if(TextUtils.isEmpty(title) && TextUtils.isEmpty(content)){
                Log.i(TAG, "onPause: not add notepad");
            }else {
                mNotepad = new NotepadContentInfo();
                mNotepad.title = mEditTitle.getText().toString();
                mNotepad.content = mEditContent.getText().toString();
                mNotepad.group = mTextGroup.getText().toString();
                mNotepad.time = String.valueOf(currentTime);
                mNotepad.imageLists = new Gson().toJson(mImagePath);
                mNotepadDataManager.insertNotepadInfo(mNotepad);
                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupInfo(mGroupInfo.groupName,++mGroupInfo.groupCount);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("groupName",mGroupInfo.groupName);
                    jsonObject.put("groupCount",mGroupInfo.groupCount);
                    SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(1);
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            if(null != images && !images.isEmpty()){
                for (int i = 0; i < images.size(); i++){
                    String path = FileTool.getImageSDCardPath()+File.separator+(i+System.currentTimeMillis())+".jpg";
                    FileTool.copyFile(images.get(i),path);
                    mImagePath.add(path);
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 纵向拼接
     * <功能详细描述>
     * @return
     */
    private Bitmap addBitmap(List<Bitmap> list) {
        int width = 0;
        int height = 0;
        float sc = 1f;
        for (Bitmap bitmap : list){
            if(null != bitmap && !bitmap.isRecycled()){
                if(width < bitmap.getWidth()){
                    width = bitmap.getWidth();
                }
                height += bitmap.getHeight();
            }
        }
        if(width > 720){
            sc = 720f / width;
            width = 720;
        }
        height = (int) (height * sc);
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);//绘制透明色
        int currentHeight = 0;
        for (Bitmap bitmap : list){
            if(null != bitmap && !bitmap.isRecycled()) {
                Rect rect = new Rect();
                rect.top = currentHeight;
                rect.left = 0;
                rect.right = (int) (bitmap.getWidth() *sc);
                rect.bottom = rect.top + (int) (bitmap.getHeight() * sc);
                currentHeight += (int) (bitmap.getHeight()*sc);
                canvas.drawBitmap(bitmap, null, rect, null);
            }
        }
        return result;
    }
}

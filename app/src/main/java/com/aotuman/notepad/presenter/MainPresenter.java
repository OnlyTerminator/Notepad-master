package com.aotuman.notepad.presenter;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.database.NotePassGroupDataManager;
import com.aotuman.notepad.base.database.NotePasswordDataManager;
import com.aotuman.notepad.base.database.NotepadDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.notepad.base.entry.PasswordInfo;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.aotuman.notepad.fragment.MainFragment;
import com.aotuman.notepad.view.SnackbarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aotuman on 2018/1/25.
 */

public class MainPresenter extends NotePresenter<MainPresenter.MainCallback> {
    public MainPresenter(MainCallback callback) {
        super(callback);
    }

    public void getNoteList(){
        Observable.create(new ObservableOnSubscribe<List>() {
            @Override
            public void subscribe(ObservableEmitter<List> emitter) throws Exception {
                GroupInfo info = getGroupInfo();
                if("密码".equals(info.groupName)){
                    emitter.onNext(NotePassGroupDataManager.getInstance(ATMApplication.getInstance()).findAllPasswordGroups2(info.groupName));
                }else {
                    emitter.onNext(NotepadDataManager.getInstance(ATMApplication.getInstance()).findNotepadByGroup(info.groupName));
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List notepadContentInfo) {
                        mCallback.updateNoteList(notepadContentInfo);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteNotepad(View view, final NotepadContentInfo notepadContentInfo) {
        SnackbarUtil.showSnackbar(view, "主人，我被删除之后是不可恢复的哦！", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.create(new ObservableOnSubscribe<List>() {
                    @Override
                    public void subscribe(ObservableEmitter<List> emitter) throws Exception {
                        GroupInfo info = getGroupInfo();
                        if("密码".equals(info.groupName)){
                            Toast.makeText(ATMApplication.getInstance(),"密码库暂不支持删除，程序猿妹妹正在努力开发中！",Toast.LENGTH_SHORT).show();
                        }else {
                            NotepadDataManager.getInstance(ATMApplication.getInstance()).deleteNotepadInfo(notepadContentInfo.id);
                            String group = (String) SPUtils.get(ATMApplication.getInstance(), SharePreEvent.GROUP_SELECTED_INFO, "");
                            try {
                                JSONObject jsonObject = new JSONObject(group);
                                jsonObject.put("groupCount",--info.groupCount);
                                jsonObject.put("groupName",info.groupName);
                                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupInfo(info.groupName,info.groupCount);
                                SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            emitter.onNext(NotepadDataManager.getInstance(ATMApplication.getInstance()).findNotepadByGroup(info.groupName));
                        }
                        emitter.onComplete();
                    }
                }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe(new Observer<List>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List notepadContentInfo) {
                                mCallback.updateNoteList(notepadContentInfo);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    private GroupInfo getGroupInfo(){
        String group = (String) SPUtils.get(ATMApplication.getInstance(), SharePreEvent.GROUP_SELECTED_INFO,"");
        final GroupInfo info = new GroupInfo("未分组",0);
        if(TextUtils.isEmpty(group)){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("groupName",info.groupName);
                jsonObject.put("groupCount",info.groupCount);
                SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JSONObject jsonObject = new JSONObject(group);
                info.groupName = jsonObject.getString("groupName");
                info.groupCount = jsonObject.getInt("groupCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return info;
    }
    public interface MainCallback extends NotePresenter.ICallback{
        void updateNoteList(List<NotepadContentInfo> list);
    }
}

package com.aotuman.notepad.presenter;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.database.NotePassGroupDataManager;
import com.aotuman.notepad.base.database.NotePasswordDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.base.entry.PassGroupInfo;
import com.aotuman.notepad.base.entry.PasswordInfo;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.aotuman.notepad.view.SnackbarUtil;
import com.aotuman.notepad.view.GroupInfoDialog;
import com.aotuman.notepad.view.PasswordInfoDialog;

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
 * Created by aotuman on 2018/2/3.
 */

public class PasswordPresenter extends NotePresenter<PasswordPresenter.NotePasswordCallback> {
    public PasswordPresenter(NotePasswordCallback callback) {
        super(callback);
    }

    public void showPasswordInfoDIalog(final Activity activity, final String groups) {
        PasswordInfoDialog.Builder builder = new PasswordInfoDialog.Builder(activity);
        PasswordInfoDialog dialog = builder
                .setPositiveButton(new PasswordInfoDialog.PositiveOnClickListener() {
                    @Override
                    public void onClick(String title, String name, String pass) {
                        PasswordInfo info = TextUtils.isEmpty(title) ? null :
                                new PasswordInfo(title, name, pass, groups);
                        doSaveInfo(activity,info,true);
                    }
                }).creat();
        dialog.setCancelable(true);
        dialog.show();
    }

    public void showPasswordInfo(final Activity activity, final PasswordInfo passwordInfo) {
        PasswordInfoDialog.Builder builder = new PasswordInfoDialog.Builder(activity);
        PasswordInfoDialog dialog = builder.setPassword(passwordInfo.password)
                .setTitle(passwordInfo.title)
                .setName(passwordInfo.name)
                .setPositiveButton(new PasswordInfoDialog.PositiveOnClickListener() {
                    @Override
                    public void onClick(String title, String name, String pass) {
                        PasswordInfo info = TextUtils.isEmpty(title) ? null :
                                new PasswordInfo(title, name, pass, passwordInfo.groups);
                        doSaveInfo(activity,info,false);
                    }
                }).creat();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void doSaveInfo(Activity activity,PasswordInfo info,boolean needAdd){
        if(null != info) {
            if(needAdd) {
                mCallback.addpasswordInfo(info);
            }
            NotePasswordDataManager manager = NotePasswordDataManager.getInstance(ATMApplication.getInstance());
            manager.insertPasswordInfo(info);
        }else {
            Toast.makeText(activity,"账号来源是必填信息哦~",Toast.LENGTH_SHORT).show();
        }
    }
    public void deletePassword(View view, final PasswordInfo passwordInfo) {
        SnackbarUtil.showSnackbar(view, "主人，我被删除之后是不可恢复的哦！", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotePasswordDataManager manager = NotePasswordDataManager.getInstance(ATMApplication.getInstance());
                manager.deletePasswordInfo(passwordInfo.title, passwordInfo.groups);
                initGroupInfo(passwordInfo.groups);
            }
        });

    }
    public void showGroupInfoDIalog(Activity activity) {
        GroupInfoDialog.Builder builder = new GroupInfoDialog.Builder(activity);
        builder.setContent("")
                .setPositiveButton(new GroupInfoDialog.PositiveOnClickListener() {
                    @Override
                    public void onClick(final String group) {
                        if (!TextUtils.isEmpty(group)) {
                            mCallback.showpassGroup(group);
                            Observable.create(new ObservableOnSubscribe<Object>() {
                                @Override
                                public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                    NotePassGroupDataManager manager = NotePassGroupDataManager.getInstance(ATMApplication.getInstance());
                                    manager.insertPassgroupInfo(new PassGroupInfo(group));
                                    String group = (String) SPUtils.get(ATMApplication.getInstance(), SharePreEvent.GROUP_SELECTED_INFO, "");
                                    try {
                                        JSONObject jsonObject = new JSONObject(group);
                                        GroupInfo info = new GroupInfo();
                                        info.groupName = jsonObject.getString("groupName");
                                        info.groupCount = jsonObject.getInt("groupCount");
                                        jsonObject.put("groupCount",++info.groupCount);
                                        NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupInfo(info.groupName,info.groupCount);
                                        SPUtils.put(ATMApplication.getInstance(),SharePreEvent.GROUP_SELECTED_INFO,jsonObject.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                                        public void onNext(Object o) {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
                        }
                    }
                }).creat().show();
    }

    public void initGroupInfo(final String groups) {
        Observable.create(new ObservableOnSubscribe<List>() {
            @Override
            public void subscribe(ObservableEmitter<List> emitter) throws Exception {
                NotePasswordDataManager manager = NotePasswordDataManager.getInstance(ATMApplication.getInstance());
                emitter.onNext(manager.findAllPassword(groups));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<List>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List list) {
                        mCallback.passwordInfos(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public interface NotePasswordCallback extends NotePresenter.ICallback {
        void passwordInfos(List<PasswordInfo> infos);

        void showpassGroup(String groups);

        void addpasswordInfo(PasswordInfo info);
    }
}

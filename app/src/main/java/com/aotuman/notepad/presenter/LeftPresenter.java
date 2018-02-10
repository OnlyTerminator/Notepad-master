package com.aotuman.notepad.presenter;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aotuman on 2018/1/24.
 */

public class LeftPresenter extends NotePresenter<LeftPresenter.LeftCallback> {

    public LeftPresenter(LeftCallback callback) {
        super(callback);
    }

    public void initLeftData(){
        String name = "TestName";
        mCallback.updatePersonalName(name);
        mCallback.updatePersonalIcon("");
        initGroupInfo();
    }

    private void initGroupInfo(){
        Observable.create(new ObservableOnSubscribe<List>() {
            @Override
            public void subscribe(ObservableEmitter<List> emitter) throws Exception {
                NoteGroupDataManager manager = NoteGroupDataManager.getInstance(ATMApplication.getInstance());
                List<GroupInfo> groupInfos = manager.findAllNotepad();
                if(null == groupInfos || groupInfos.isEmpty()) {
                    groupInfos = new ArrayList<>();
                    groupInfos.add(new GroupInfo("未分组",0));
                    groupInfos.add(new GroupInfo("生活",0));
                    groupInfos.add(new GroupInfo("工作",0));
                    groupInfos.add(new GroupInfo("密码",0));
                    manager.initGroupInfos(groupInfos);
                }
                emitter.onNext(groupInfos);
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
                  mCallback.updateGroupView(list);
              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onComplete() {

              }
          });
    }

    public interface LeftCallback extends NotePresenter.ICallback{
        void updateGroupView(List<GroupInfo> list);

        void updatePersonalName(String name);

        void updatePersonalIcon(String path);
    }
}

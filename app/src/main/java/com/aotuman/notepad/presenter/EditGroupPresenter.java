package com.aotuman.notepad.presenter;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aotuman.notepad.ATMApplication;
import com.aotuman.notepad.R;
import com.aotuman.notepad.activity.EditGroupActivity;
import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.database.NoteGroupDataManager;
import com.aotuman.notepad.base.entry.GroupInfo;
import com.aotuman.notepad.view.GroupInfoDialog;
import com.aotuman.notepad.view.SnackbarUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aotuman on 2018/1/24.
 */

public class EditGroupPresenter extends NotePresenter<EditGroupPresenter.EditCallback> {
    public EditGroupPresenter(EditCallback callback) {
        super(callback);
    }

    public void getGroupInfo(){
        Observable.just(NoteGroupDataManager.getInstance(ATMApplication.getInstance()).findAllNotepad())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    public void addNoteGroup(Activity activity,final String content){
        GroupInfoDialog.Builder builder = new GroupInfoDialog.Builder(activity);
        builder.setContent(content)
                .setPositiveButton(new GroupInfoDialog.PositiveOnClickListener() {
                    @Override
                    public void onClick(String group) {
                        if (TextUtils.isEmpty(content) && !TextUtils.isEmpty(group)) {
                            NoteGroupDataManager.getInstance(ATMApplication.getInstance()).insertGroupInfo(new GroupInfo(group, 0));
                            getGroupInfo();
                        } else if (!TextUtils.isEmpty(content)) {
                            if (TextUtils.isEmpty(group)) {
                                Toast.makeText(ATMApplication.getInstance(), "修改失败，分组名不可为空！", Toast.LENGTH_SHORT).show();
                            } else if (!content.equals(group)) {
                                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).updateGroupName(group, content);
                                getGroupInfo();
                            }
                        }
                    }
                }).creat().show();
    }

    public void deleteNoteGroup(View view,final GroupInfo groupInfo){
        SnackbarUtil.showSnackbar(view, "主人，您真的不需要我了吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteGroupDataManager.getInstance(ATMApplication.getInstance()).deleteNotepadInfo(groupInfo.groupName);
                getGroupInfo();
            }
        });
    }

    public interface EditCallback extends NotePresenter.ICallback{
        void updateGroupView(List<GroupInfo> list);
    }
}

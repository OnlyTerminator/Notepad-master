package com.aotuman.notepad.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.aotuman.notepad.base.NotePresenter;
import com.aotuman.notepad.base.entry.NotepadContentInfo;
import com.aotuman.share.CommonUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aotuman on 2018/1/27.
 */

public class NotepadSharePresenter extends NotePresenter<NotepadSharePresenter.ShareBack> {

    public NotepadSharePresenter(ShareBack callback) {
        super(callback);
    }

    public void initData(Intent intent){
        if(null != intent) {
            NotepadContentInfo info = (NotepadContentInfo) intent.getSerializableExtra("notepad");
            if(null != info) {
                mCallback.showShareView(info);
            }
        }
    }

    public void prepareShareBitmap(final View view,final String path){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                Bitmap bitmap = loadBitmapFromView(view,true);
                CommonUtils.writeBitmap(new File(path),bitmap,100,true);
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
                        mCallback.sharePrepareResult(false);
                    }

                    @Override
                    public void onComplete() {
                        mCallback.sharePrepareResult(true);
                    }
                });
    }

    private Bitmap loadBitmapFromView(View v, boolean isParemt) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        c.translate(-v.getScrollX(), -v.getScrollY());
        v.draw(c);
        return screenshot;
    }
    public interface ShareBack extends NotePresenter.ICallback{
        void showShareView(NotepadContentInfo info);
        void sharePrepareResult(boolean flag);
    }
}

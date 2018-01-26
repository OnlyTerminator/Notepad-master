package com.aotuman.notepad.base;

/**
 * Created by aotuman on 2018/1/24.
 */

public class NotePresenter<C extends NotePresenter.ICallback> {
    protected C mCallback;

    public NotePresenter(C callback){
        mCallback = callback;
    }

    public interface ICallback {

    }
}

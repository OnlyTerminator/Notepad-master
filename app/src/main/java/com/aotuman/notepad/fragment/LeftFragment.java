package com.aotuman.notepad.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aotuman.notepad.R;

/**
 * Created by aotuman on 2017/5/3.
 */

public class LeftFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_left, container, false);
        }
        return view;
    }
}

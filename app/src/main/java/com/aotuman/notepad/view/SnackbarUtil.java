package com.aotuman.notepad.view;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.aotuman.notepad.R;

public class SnackbarUtil {

    public static void showSnackbar(View view, String des, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, des, Snackbar.LENGTH_LONG).setAction("确定", listener);
        snackbar.setActionTextColor(0xffffffff);
        setSnackbarColor(snackbar, 0xffffffff, 0xfff44336);
        snackbar.show();

    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar
     * @param backgroundColor
     */
    private static void setSnackbarColor(Snackbar snackbar, int messageColor, int backgroundColor) {
        View view = snackbar.getView();//获取Snackbar的view
        if (view != null) {
            view.setBackgroundColor(backgroundColor);//修改view的背景色
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);//获取Snackbar的message控件，修改字体颜色
        }
    }
}

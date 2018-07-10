package com.aotuman.notepad.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aotuman.notepad.R;

/**
 * Created by aotuman on 2017/5/16.
 */

public class PasswordInfoDialog extends Dialog {
    public PasswordInfoDialog(Context context) {
        super(context);
    }

    public PasswordInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PasswordInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mName;
        private String mPassword;
        private PositiveOnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setPassword(String password) {
            this.mPassword = password;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setName(String name) {
            this.mName = name;
            return this;
        }

        public Builder setPositiveButton(PositiveOnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public PasswordInfoDialog creat() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PasswordInfoDialog dialog = new PasswordInfoDialog(mContext, R.style.Group_Dialog_Light);
            View view = inflater.inflate(R.layout.password_edit_dialog, null);
            dialog.addContentView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText editPass = (EditText) view.findViewById(R.id.et_password);
            final EditText editTitle = (EditText) view.findViewById(R.id.et_title);
            final EditText editName = (EditText) view.findViewById(R.id.et_name);
            Button button = (Button) view.findViewById(R.id.btn_sure);
            if (!TextUtils.isEmpty(mTitle)) {
                editTitle.setText(mTitle);
            }

            if (!TextUtils.isEmpty(mPassword)) {
                editPass.setText(mPassword);
            }

            if (!TextUtils.isEmpty(mName)) {
                editName.setText(mName);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != positiveButtonClickListener) {
                        String title = editTitle.getText().toString().trim();
                        String name = editName.getText().toString().trim();
                        String pass = editPass.getText().toString().trim();
                        positiveButtonClickListener.onClick(title,name,pass);
                    }
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            return dialog;
        }
    }

    public interface PositiveOnClickListener {
        void onClick(String title,String name,String pass);
    }
}

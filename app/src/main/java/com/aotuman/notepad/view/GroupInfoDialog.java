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

public class GroupInfoDialog extends Dialog {
    public GroupInfoDialog(Context context) {
        super(context);
    }

    public GroupInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected GroupInfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mContent;
        private View mContentView;
        private PositiveOnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.mContentView = v;
            return this;
        }

        public Builder setPositiveButton(PositiveOnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public GroupInfoDialog creat() {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final GroupInfoDialog dialog = new GroupInfoDialog(mContext, R.style.Group_Dialog_Light);
            View view = inflater.inflate(R.layout.group_edit_dialog, null);
            dialog.addContentView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText editText = (EditText) view.findViewById(R.id.et_content);
            Button button = (Button) view.findViewById(R.id.btn_sure);
            if (!TextUtils.isEmpty(mTitle)) {
            }
            if (!TextUtils.isEmpty(mContent)) {
                editText.setText(mContent);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveButtonClickListener.onClick(editText.getText().toString().trim());
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
        void onClick(String content);
    }
}

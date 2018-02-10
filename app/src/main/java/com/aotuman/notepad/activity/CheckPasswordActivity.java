package com.aotuman.notepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.aotuman.notepad.R;
import com.aotuman.notepad.base.utils.SPUtils;
import com.aotuman.notepad.base.utils.SharePreEvent;
import com.leo.gesturelibray.enums.LockMode;
import com.leo.gesturelibray.view.CustomLockView;

/**
 * Created by aotuman on 2018/2/3.
 */

public class CheckPasswordActivity extends AppCompatActivity {
    private CustomLockView mCustomLockView;
    private TextView mTvHint;
    private String mPassGroups;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);
        initView();
        initData();
        initAction();
    }

    private void initData() {
        //设置模式
        LockMode lockMode = (LockMode) getIntent().getSerializableExtra("check_mode");
        mPassGroups = getIntent().getStringExtra("groups");
        if(null != lockMode) {
            mCustomLockView.setMode(lockMode);
            switch (lockMode) {
                case SETTING_PASSWORD:
                    mTvHint.setText("请输入要设置的密码");
                    break;
                case VERIFY_PASSWORD:
                    mTvHint.setText("请输入已经设置过的密码");
                    String str = (String) SPUtils.get(this, SharePreEvent.CHECK_PASS_WORD,"");
                    mCustomLockView.setOldPassword(str);
                    break;
            }
        }
    }

    private void initAction() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.back);
        mActionBar.setTitle(R.string.password_check);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        //处理其他菜单点击事件
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mCustomLockView = (CustomLockView) findViewById(R.id.lv_lock);
        mTvHint = (TextView) findViewById(R.id.tv_hint);
        //显示绘制方向
        mCustomLockView.setShow(true);
        //允许最大输入次数
        mCustomLockView.setErrorNumber(3);
        //密码最少位数
        mCustomLockView.setPasswordMinLength(4);
        //编辑密码或设置密码时，是否将密码保存到本地，配合setSaveLockKey使用
        mCustomLockView.setSavePin(true);
        //保存密码Key
        mCustomLockView.setSaveLockKey("PASS_KEY_MAP");
        mCustomLockView.setOnCompleteListener(onCompleteListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(1);
        finish();
    }

    /**
     * 密码输入监听
     */
    CustomLockView.OnCompleteListener onCompleteListener = new CustomLockView.OnCompleteListener() {
        @Override
        public void onComplete(String password, int[] indexs) {
            if(!TextUtils.isEmpty(password)){
                SPUtils.put(CheckPasswordActivity.this, SharePreEvent.CHECK_PASS_WORD,password);
            }
            mTvHint.setText(getPassWordHint());
            Intent intent = new Intent(CheckPasswordActivity.this,PassWordActivity.class);
            intent.putExtra("groups",mPassGroups);
            startActivityForResult(intent,1);
        }

        @Override
        public void onError(String errorTimes) {
            mTvHint.setText("密码错误，还可以输入" + errorTimes + "次");
        }

        @Override
        public void onPasswordIsShort(int passwordMinLength) {
            mTvHint.setText("密码不能少于" + passwordMinLength + "个点");
        }

        @Override
        public void onAginInputPassword(LockMode mode, String password, int[] indexs) {
            mTvHint.setText("请再次输入密码");
        }


        @Override
        public void onInputNewPassword() {
            mTvHint.setText("请输入新密码");
        }

        @Override
        public void onEnteredPasswordsDiffer() {
            mTvHint.setText("两次输入的密码不一致");
        }

        @Override
        public void onErrorNumberMany() {
            mTvHint.setText("密码错误次数超过限制，不能再输入");
        }

    };

    /**
     * 密码相关操作完成回调提示
     */
    private String getPassWordHint() {
        String str = null;
        switch (mCustomLockView.getMode()) {
            case SETTING_PASSWORD:
                str = "密码设置成功";
                break;
            case EDIT_PASSWORD:
                str = "密码修改成功";
                break;
            case VERIFY_PASSWORD:
                str = "密码正确";
                break;
            case CLEAR_PASSWORD:
                str = "密码已经清除";
                break;
        }
        return str;
    }
}

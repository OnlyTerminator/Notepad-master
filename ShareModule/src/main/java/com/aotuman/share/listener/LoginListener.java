package com.aotuman.share.listener;

import com.aotuman.share.entity.ThirdLoginInfo;

import java.io.Serializable;

/**
 * Created by aotuman on 2017/6/30.
 */

public interface LoginListener extends Serializable{

    void onSuccess(ThirdLoginInfo thirdLoginInfo);

    void onError();

    void onCancel();
}

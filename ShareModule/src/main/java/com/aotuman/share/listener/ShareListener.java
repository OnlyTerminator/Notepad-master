package com.aotuman.share.listener;

import com.aotuman.share.entity.ShareChannelType;

/**
 * Created by aotuman on 2017/6/30.
 */

public interface ShareListener{

    void onSuccess(ShareChannelType type);

    void onError(ShareChannelType type);

    void onCancel(ShareChannelType type);
}

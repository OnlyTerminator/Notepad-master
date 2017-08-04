package com.aotuman.share.entity;
/**
 * Created by aotuman on 2017/7/4.
 */

public class ShareBackEvent {
    public int  mState;  // 0 1 2登录成功  失败  取消
    public String  mDesc;
    public ShareBackEvent(int state){
        mState = state;
        switch (state){
            case 0:
                mDesc = "share success";
                break;
            case 1:
                mDesc = "share failed";
                break;
            case 2:
                mDesc = "share cancel";
                break;
            default:
                mDesc = "share exception";
                break;
        }
    }
}

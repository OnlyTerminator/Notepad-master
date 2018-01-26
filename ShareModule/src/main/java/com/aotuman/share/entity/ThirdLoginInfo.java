package com.aotuman.share.entity;

import java.io.Serializable;

/**
 * Created by aotuman on 2017/07/03.
 */
public class ThirdLoginInfo implements Serializable{
    /**
     * 授权token
     */
    public String access_token;
    /**
     * 登录名(腾讯账户 blogid,新浪账户登录名)
     */
    public String uid;
    /**
     * 第三方平台返回的用户信息json
     */
    public String thirdJson;
}

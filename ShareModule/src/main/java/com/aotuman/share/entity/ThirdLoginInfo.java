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
    public String login_name;
    //以下参数均为三方登陆所需
    public String nick;
    public String face;
    public String sex;
}

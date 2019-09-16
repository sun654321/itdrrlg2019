package com.service;

import com.common.ResponseCode;
import com.pojo.User;

public interface UserService {
    //用户登录
    ResponseCode login(String username, String password);
    //用户注册
    ResponseCode register(User user);
    //检查用户名是否有效
    ResponseCode checkvalid(String str, String type);
    //忘记密码
    ResponseCode forgetgetquestion(String username);
    //提交问题答案
    ResponseCode forgetcheckanswer(String username, String question, String answer);
    //获取当前登录用户的详细信息
    ResponseCode getinforamtion(Integer id, String username);
    //登录状态更新个人信息
    ResponseCode updateinformation(User u);
    //忘记密码的重设密码
    ResponseCode forgetresetpassword(String username, String passwordNew, String forgetToken);
    //登录中状态重置密码
    ResponseCode resetpassword(String passwordOld, String passwordNew,Integer id);
}

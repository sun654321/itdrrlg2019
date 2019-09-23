package com.service.impl;

import com.common.Const;
import com.common.ResponseCode;
import com.common.TokenCache;
import com.mappers.UserMapper;
import com.pojo.User;
import com.service.UserService;
import com.utils.MD5Util;
import com.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
     UserMapper userMapper;


    //用户登录
    @Override
    public ResponseCode<User> login(String username, String password) {
        if (username == null || username.equals("")) {

            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_USERNAME.getCode(),Const.UsersEnum.EMPTY_USERNAME.getDesc());
        }

        if (password == null || password.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }

        User user = userMapper.login(username);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.INEXISTENCE_USER.getCode(), Const.UsersEnum.INEXISTENCE_USER.getDesc());
        }
        //使用MD5加密
        String md5Password = MD5Utils.getMD5Code(password);
        //判断登录
        User user1 = userMapper.login1(username, md5Password);
        if (user1 == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.ERROR_PASSWORD.getCode(), Const.UsersEnum.ERROR_PASSWORD.getDesc());
        }

        ResponseCode rs = ResponseCode.seccessRs(user1);
        return rs;
    }

    //用户注册
    @Override
    public ResponseCode register(User user) {
        if (user.getUsername() == null || user.getUsername().equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }
        //验证用户名是否存在
        User user1 = userMapper.login(user.getUsername());
        if (user1 != null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EXIST_USER.getCode(), Const.UsersEnum.EXIST_USER.getDesc());
        }
        User email = userMapper.email(user.getEmail());
        if (email != null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EXIST_EMAIL.getCode(), Const.UsersEnum.EXIST_EMAIL.getDesc());
        }
        //使用MD5加密
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        //输入注册信息
        int register = userMapper.register(user);
        if (register >0) {
            return ResponseCode.seccessRs(Const.UsersEnum.SUCCESS_USER.getCode(), Const.UsersEnum.SUCCESS_USER.getDesc());
        }
        return ResponseCode.notseccessRs(Const.UsersEnum.NOSUCCESS_USER.getCode(), Const.UsersEnum.NOSUCCESS_USER.getDesc());
    }

    //检查用户名是否有效
    @Override
    public ResponseCode checkvalid(String str, String type) {
        if (str == null || str.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_XINX.getCode(),Const.UsersEnum.EMPTY_XINX.getDesc());
        }
        if (type == null || type.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_LX.getCode(), Const.UsersEnum.EMPTY_LX.getDesc());
        }
        User es = userMapper.usernameemail(str, type);

        if (es != null && type.equals("username")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EXIST_USER.getCode(), Const.UsersEnum.EXIST_USER.getDesc());
        }
        if (es != null && type.equals("email")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EXIST_EMAIL.getCode(),Const.UsersEnum.EXIST_EMAIL.getDesc());
        }

        return ResponseCode.seccessRs(Const.UsersEnum.SUCCESS_MSG.getCode(),Const.UsersEnum.SUCCESS_MSG.getDesc());
    }

    //获取当前登录用户的详细信息
    @Override
    public ResponseCode getinforamtion(Integer id, String username) {
        User user = userMapper.getinforamtion(id, username);
        user.setPassword("");
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.FORCE_EXIT.getCode(), Const.UsersEnum.FORCE_EXIT.getDesc());
        }
        return ResponseCode.seccessRs(user);
    }

    //登录状态更新个人信息
    @Override
    public ResponseCode updateinformation(User u) {
        //查询邮箱
        User user = userMapper.email1(u.getEmail(), u.getId());
        if (user != null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EXIST_EMAIL.getCode(),Const.UsersEnum.EXIST_EMAIL.getDesc());
        }
        int s = userMapper.updateinformation(u);
        if (s > 0) {
            return ResponseCode.seccessRs(Const.UsersEnum.SUCCESS_USERMSG.getCode(),Const.UsersEnum.SUCCESS_USERMSG.getDesc());
        }
        return ResponseCode.notseccessRs(Const.UsersEnum.NOSUCCESS_USERMSG.getCode(), Const.UsersEnum.NOSUCCESS_USERMSG.getDesc());
    }

    //忘记密码的重设密码
    @Override
    public ResponseCode forgetresetpassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_USERNAME.getCode(),Const.UsersEnum.EMPTY_USERNAME.getDesc());
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }
        if (forgetToken == null || forgetToken.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.KONG_EFFICACY.getCode(),Const.UsersEnum.KONG_EFFICACY.getDesc());
        }

        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.INEXISTENCE_USER.getCode(), Const.UsersEnum.INEXISTENCE_USER.getDesc());
        }

        //判断令牌
        String s = TokenCache.get("token_" + username);

       if(s==null||s.equals("")){
           return ResponseCode.notseccessRs(Const.UsersEnum.LOSE_EFFICACY.getCode(),Const.UsersEnum.LOSE_EFFICACY.getDesc());
       }
       if(!forgetToken.equals(s)){
           return ResponseCode.notseccessRs(Const.UsersEnum.UNLAWFULNESS_TOKEN.getCode(),Const.UsersEnum.UNLAWFULNESS_TOKEN.getDesc());
       }
        String code = MD5Utils.getMD5Code(passwordNew);
        int i = userMapper.forgetresetpassword(username, code);
        if (i <= 0) {
            return ResponseCode.notseccessRs(Const.UsersEnum.DEFEACTED_PASSWORDNEW.getCode(),Const.UsersEnum.DEFEACTED_PASSWORDNEW.getDesc());
        }
        return ResponseCode.seccessRs(Const.UsersEnum.SUCCESS_PASSWORDNEW.getCode(), Const.UsersEnum.SUCCESS_PASSWORDNEW.getDesc());
    }

    //登录中状态重置密码
    @Override
    public ResponseCode resetpassword(String passwordOld, String passwordNew, Integer id) {
        if (passwordOld == null || passwordOld.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_PASSWORD.getCode(),Const.UsersEnum.EMPTY_PASSWORD.getDesc());
        }
       //验证旧密码
        String md5Code = MD5Utils.getMD5Code(passwordOld);
        User user = userMapper.resetpassword(md5Code, id);
        if (user==null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.ERROR_PASSWORDOLD.getCode(),Const.UsersEnum.ERROR_PASSWORDOLD.getDesc());
        }
        //设置新密码
        String Code = MD5Utils.getMD5Code(passwordNew);
       int i =userMapper.resetpassword1(Code, id);
       if(i<=0){
           return  ResponseCode.notseccessRs(Const.UsersEnum.DEFEACTED_PASSWORDNEW.getCode(),Const.UsersEnum.DEFEACTED_PASSWORDNEW.getDesc());
       }
        return  ResponseCode.seccessRs(Const.UsersEnum.SUCCESS_PASSWORDNEW.getCode(),Const.UsersEnum.SUCCESS_PASSWORDNEW.getDesc());
    }


    //忘记密码
    @Override
    public ResponseCode forgetgetquestion(String username) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_USERNAME.getCode(),Const.UsersEnum.EMPTY_USERNAME.getDesc());
        }
        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.INEXISTENCE_USER.getCode(), Const.UsersEnum.INEXISTENCE_USER.getDesc());
        } else {
            String s = userMapper.forgetquestion(username);
            if (s == null || s.equals("")) {
                return ResponseCode.notseccessRs(Const.UsersEnum.NO_QUESTION.getCode(), Const.UsersEnum.NO_QUESTION.getDesc());
            }
            return ResponseCode.seccessRs(s);
        }
    }

    //提交问题答案
    @Override
    public ResponseCode forgetcheckanswer(String username, String question, String answer) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_USERNAME.getCode(),Const.UsersEnum.EMPTY_USERNAME.getDesc());
        }
        if (question == null || question.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_QUESTION.getCode(), Const.UsersEnum.EMPTY_QUESTION.getDesc());
        }
        if (answer == null || answer.equals("")) {
            return ResponseCode.notseccessRs(Const.UsersEnum.EMPTY_ANSWER.getCode(), Const.UsersEnum.EMPTY_ANSWER.getDesc());
        }
        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.INEXISTENCE_USER.getCode(), Const.UsersEnum.INEXISTENCE_USER.getDesc());
        }
        User user = userMapper.forgetcheckanswer(username, question, answer);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.ERROR_ANSWER.getCode(), Const.UsersEnum.ERROR_ANSWER.getDesc());
        }
        //产生随机字符令牌
        //UUID是通用的标识符
        String token = UUID.randomUUID().toString();
        TokenCache.set("token_" + username, token);
        return ResponseCode.seccessRs(token);

    }


}

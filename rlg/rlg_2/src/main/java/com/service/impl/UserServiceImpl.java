package com.service.impl;

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
    private UserMapper userMapper;


    //用户登录
    @Override
    public ResponseCode<User> login(String username, String password) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs("用户名不能为空");
        }
        if (password == null || password.equals("")) {
            return ResponseCode.notseccessRs("密码不能为空");
        }
        User user = userMapper.login(username);
        if (user == null) {
            return ResponseCode.notseccessRs(101, "用户名不存在");
        }
        //使用MD5加密
        String md5Password = MD5Utils.getMD5Code(password);
        //判断登录
        User user1 = userMapper.login1(username, md5Password);
        if (user1 == null) {
            return ResponseCode.notseccessRs(1, "密码错误");
        }

        ResponseCode rs = ResponseCode.seccessRs(user1);
        return rs;
    }

    //用户注册
    @Override
    public ResponseCode register(User user) {
        if (user.getUsername() == null || user.getUsername().equals("")) {
            return ResponseCode.notseccessRs("用户名为空");
        }
        if (user.getPassword() == null || user.getPassword().equals("")) {
            return ResponseCode.notseccessRs("密码为空");
        }
        //验证用户名是否存在
        User user1 = userMapper.login(user.getUsername());
        if (user1 != null) {
            return ResponseCode.notseccessRs(1, "用户名已经被使用");
        }
        User email = userMapper.email(user.getEmail());
        if (email != null) {
            return ResponseCode.notseccessRs(1, "邮箱已经被使用");
        }
        //使用MD5加密
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        //输入注册信息
        int register = userMapper.register(user);
        if (register > 0) {
            return ResponseCode.seccessRs(0, "注册成功");
        }
        return ResponseCode.notseccessRs(1, "注册失败");
    }

    //检查用户名是否有效
    @Override
    public ResponseCode checkvalid(String str, String type) {
        if (str == null || str.equals("")) {
            return ResponseCode.notseccessRs("输入的信息为空");
        }
        if (type == null || type.equals("")) {
            return ResponseCode.notseccessRs(10, "输入的类型为空");
        }
        User es = userMapper.usernameemail(str, type);

        if (es != null && type.equals("username")) {
            return ResponseCode.notseccessRs(1, "用户名已存在");
        }
        if (es != null && type.equals("email")) {
            return ResponseCode.notseccessRs(1, "邮箱已注册");
        }

        return ResponseCode.seccessRs("校验成功，可以使用");
    }

    //获取当前登录用户的详细信息
    @Override
    public ResponseCode getinforamtion(Integer id, String username) {
        User user = userMapper.getinforamtion(id, username);
        user.setPassword("");
        if (user == null) {
            return ResponseCode.notseccessRs("无法获取当前用户信息");
        }
        return ResponseCode.seccessRs(user);
    }

    //登录状态更新个人信息
    @Override
    public ResponseCode updateinformation(User u) {
        //查询邮箱
        User user = userMapper.email1(u.getEmail(), u.getId());
        if (user != null) {
            return ResponseCode.notseccessRs("邮箱已经被使用");
        }
        int s = userMapper.updateinformation(u);
        if (s > 0) {
            return ResponseCode.seccessRs("修改成功");
        }
        return ResponseCode.notseccessRs(1, "修改失败");
    }

    //忘记密码的重设密码
    @Override
    public ResponseCode forgetresetpassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs("用户名为空");
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ResponseCode.notseccessRs("新密码为空");
        }
        if (forgetToken == null || forgetToken.equals("")) {
            return ResponseCode.notseccessRs("令牌参数为空");
        }

        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(101, "用户名不存在");
        }

        //判断令牌
        String s = TokenCache.get("token_" + username);

       if(s==null||s.equals("")){
           return ResponseCode.notseccessRs("Token过期了");
       }
       if(!forgetToken.equals(s)){
           return ResponseCode.notseccessRs("非法的Token");
       }
        String code = MD5Utils.getMD5Code(passwordNew);
        int i = userMapper.forgetresetpassword(username, code);
        if (i <= 0) {
            return ResponseCode.notseccessRs("修改密码失败");
        }
        return ResponseCode.seccessRs(0, "修改密码成功");
    }

    //登录中状态重置密码
    @Override
    public ResponseCode resetpassword(String passwordOld, String passwordNew, Integer id) {
        if (passwordOld == null || passwordOld.equals("")) {
            return ResponseCode.notseccessRs(1,"旧密码为空");
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ResponseCode.notseccessRs(1,"新密码为空");
        }
       //验证旧密码
        String md5Code = MD5Utils.getMD5Code(passwordOld);
        User user = userMapper.resetpassword(md5Code, id);
        if (user==null) {
            return ResponseCode.notseccessRs(100,"旧密码输入错误");
        }
        //设置新密码
        String Code = MD5Utils.getMD5Code(passwordNew);
       int i =userMapper.resetpassword1(Code, id);
       if(i<=0){
           return  ResponseCode.notseccessRs(100,"修改失败");
       }
        return  ResponseCode.seccessRs(0,"修改成功");
    }


    //忘记密码
    @Override
    public ResponseCode forgetgetquestion(String username) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs(1,"用户名不能为空");
        }
        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(1, "用户名不存在");
        } else {
            String s = userMapper.forgetquestion(username);
            if (s == null || s.equals("")) {
                return ResponseCode.notseccessRs(100, "该用户未设置找回密码问题");
            }
            return ResponseCode.seccessRs(s);
        }
    }

    //提交问题答案
    @Override
    public ResponseCode forgetcheckanswer(String username, String question, String answer) {
        if (username == null || username.equals("")) {
            return ResponseCode.notseccessRs(1,"用户名不能为空");
        }
        if (question == null || question.equals("")) {
            return ResponseCode.notseccessRs(1, "问题不能为空");
        }
        if (answer == null || answer.equals("")) {
            return ResponseCode.notseccessRs(1, "答案不能为空");
        }
        User login = userMapper.login(username);
        if (login == null) {
            return ResponseCode.notseccessRs(101, "用户名不存在");
        }
        User user = userMapper.forgetcheckanswer(username, question, answer);
        if (user == null) {
            return ResponseCode.notseccessRs(100, "问题答案错误");
        }
        //产生随机字符令牌
        //UUID是通用的标识符
        String token = UUID.randomUUID().toString();
        TokenCache.set("token_" + username, token);
        return ResponseCode.seccessRs(token);

    }


}

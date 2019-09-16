package com.mappers;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    //验证用户名是否存在
    User login(@Param("username") String username);

    //验证密码是否正确
    User login1(@Param("username") String username, @Param("password") String password);

    //验证邮箱是否被使用
    User email( String email);

    //用户注册
    int register( User user);

    //检查用户名是否有效
    User usernameemail(@Param("str") String str, @Param("type") String type);

    //忘记密码
    String forgetquestion(@Param("username") String username);

    //根据问题,答案验证是否存在该用户
    User forgetcheckanswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    //获取当前登录用户的详细信息
    User getinforamtion(@Param("id") Integer id, @Param("username") String username);

    //登录状态更新个人信息
    int updateinformation( User u);

    //查询不是自己的邮箱
    User email1(@Param("email") String email, @Param("id") Integer id);

    //忘记密码的重设密码
    int forgetresetpassword(@Param("username") String username,@Param("passwordNew") String passwordNew);

    //验证旧密码
    User resetpassword(@Param("passwordOld")String passwordOld, @Param("id")Integer id);

    //设置新密码
    int resetpassword1(@Param("passwordNew")String passwordNew,@Param("id") Integer id);
}
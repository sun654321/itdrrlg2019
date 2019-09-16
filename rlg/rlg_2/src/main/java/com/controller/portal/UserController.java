package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.pojo.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    //用户登录
    @RequestMapping("login.do")
    public ResponseCode<User> login(String username, String password, HttpSession session) {
        ResponseCode<User> rs = userService.login(username, password);
        //根据状态码判断是否成功
        if (rs.isSuccess()) {
            User user1 = rs.getData();
            //把数据放到session里
            session.setAttribute("users", user1);
            User user2 = new User();
            user2.setId(user1.getId());
            user2.setUsername(user1.getUsername());
            user2.setPassword("");
            user2.setEmail(user1.getEmail());
            user2.setPhone(user1.getPhone());
            user2.setCreateTime(user1.getCreateTime());
            user2.setUpdateTime(user1.getUpdateTime());
            //把session在存储到rs
            rs.setData(user2);
        }
        return rs;
    }

    //用户注册
    @RequestMapping("register.do")
    public ResponseCode register(User user) {
        ResponseCode rs = userService.register(user);
        return rs;
    }

    //检查用户名是否有效
    @RequestMapping("check_valid.do")
    public ResponseCode checkvalid(String str, String type) {
        ResponseCode rs = userService.checkvalid(str, type);
        return rs;
    }

    //获取登录用户信息
    @RequestMapping("get_user_info.do")
    public ResponseCode<User> getuserinfo(HttpSession session) {
        User user1 = (User) session.getAttribute(Const.CURRENTUSER);
        if (user1 == null) {
            return ResponseCode.notseccessRs(2, "用户未登录");
        }
        user1.setPassword("");
        return ResponseCode.seccessRs(user1);
    }

    //退出登录
    @RequestMapping("logout.do")
    public ResponseCode<User> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENTUSER);
        return ResponseCode.seccessRs(0,"退出成功");
    }

    //获取当前登录用户的详细信息
    @RequestMapping("get_inforamtion.do")
    public ResponseCode getinforamtion(HttpSession session) {
        User user1 = (User) session.getAttribute(Const.CURRENTUSER);
        if(user1!=null){
            ResponseCode rs = userService.getinforamtion(user1.getId(),user1.getUsername());
        return rs;
        }
        return ResponseCode.notseccessRs("用户未登录，无法获取当前用户信息");
    }

    //登录状态更新个人信息
    @RequestMapping("update_information.do")
    public ResponseCode updateinformation(User u, HttpSession session) {
        User user1 = (User) session.getAttribute(Const.CURRENTUSER);
        if (user1 != null) {
            u.setId(user1.getId());
            u.setUsername(user1.getUsername());
            ResponseCode rs = userService.updateinformation(u);
            session.setAttribute(Const.CURRENTUSER,u);
            return rs;
        }
        return ResponseCode.notseccessRs("用户未登录，无法获取当前用户信息");
    }

     //忘记密码
    @RequestMapping("forget_get_question.do")
    public ResponseCode forgetgetquestion(String username) {
        ResponseCode rs = userService.forgetgetquestion(username);
        return rs;
    }

    //提交问题答案
    @RequestMapping("forget_check_answer.do")
    public ResponseCode forgetcheckanswer(String username, String question, String answer) {
        ResponseCode rs = userService.forgetcheckanswer(username, question, answer);
        return rs;
    }

    //忘记密码的重设密码
    @RequestMapping("forget_reset_password.do")
    public ResponseCode forgetresetpassword(String username, String passwordNew, String  forgetToken) {
        ResponseCode rs = userService.forgetresetpassword(username, passwordNew, forgetToken);
        return rs;
    }

//登录中状态重置密码
    @RequestMapping("reset_password.do")
    public ResponseCode resetpassword(String passwordOld,
                                          String passwordNew,
                                          HttpSession session) {
        User user1 = (User) session.getAttribute(Const.CURRENTUSER);
        if (user1 != null) {
            ResponseCode rs = userService.resetpassword(passwordOld,passwordNew,user1.getId());

            session.setAttribute(Const.CURRENTUSER,user1);
            return rs;
        }
        return ResponseCode.notseccessRs("用户未登录，无法获取当前用户信息");
    }


}

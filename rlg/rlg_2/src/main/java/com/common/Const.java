package com.common;


public class Const {
    public static final String CURRENTUSER = "users";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String AUTOLOGINTOKEN = "autoLoginToken";
    public static final String JESSESSIOMID_COOKIE = "JESSESSIOMID_COOKIE";
    public static final String Email = "email";
    public static final String USERNAME = "username";


    //成功时通用状态码
    public static final int SUCESS = 0;
    //失败时通用状态码
    public static final int ERROR = 1;
    //未登录
    public static final int DL = 2;
    //输入的信息为空
    public static final int KONG = 10;
    //错误
    public static final int CW = 20;
    //被使用
    public static final int BSY= 40;
    //Token过期了
    public static final int GQ= 40;
    //非法的Token
    public static final int FFD= 40;
    //该用户未设置找回密码问题
    public static final int WZD= 40;

    public  interface  cart{
        String  LLIMITQUANTITYSUCCESS="LIMIT_NUM_SUCCESS";
        String  LLIMITQUANTITYNOT="LIMIT_NUM_NOT_SUCCESS";
        Integer  UNCHECK=0;
        Integer  CHECK=1;
    }


    public enum  UsersEnum {

        NEED_LOGIN( DL , "未登录"),
        MMCW( ERROR , "密码错误"),
        YHMKONG(KONG,"用户名为空"),
        MMKONG(KONG,"密码为空"),
        YHMBCZ(KONG,"用户名不存在"),
        YHMBSY(BSY,"用户名已经被使用"),
        YXBSY(BSY,"邮箱已经被使用"),
        SRDCSYW(KONG,"输入的参数有误");



        private int code;
        private String desc;
        private UsersEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

    public  enum  CartCheckedEnum{

        PRODUCT_CHECKED(1,"已勾选"),
        PRODUCT_UNCHECKED(0,"未勾选")
        ;


        private  int  code;
        private String desc;
        private CartCheckedEnum(int code,String desc){
            this.code=code;
            this.desc=desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

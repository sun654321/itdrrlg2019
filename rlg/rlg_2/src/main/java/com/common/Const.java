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


    public interface cart {
        String LLIMITQUANTITYSUCCESS = "LIMIT_NUM_SUCCESS";
        String LLIMITQUANTITYNOT = "LIMIT_NUM_NOT_SUCCESS";
        Integer UNCHECK = 0;
        Integer CHECK = 1;
    }


    //用户模块状态码
    public enum UsersEnum {
        ERROR_PASSWORD(StatusCode.ERROR_PASSWORD, "密码错误"),
        EMPTY_XINX(StatusCode.ERROR, "输入的信息不能为空"),
        EMPTY_LX(StatusCode.ERROR, "输入的类型不能为空"),
        EMPTY_USERNAME(StatusCode.ERROR, "用户名不能为空"),
        EMPTY_PASSWORD(StatusCode.ERROR, "密码不能为空"),
        EMPTY_QUESTION(StatusCode.ERROR, "问题不能为空"),
        EMPTY_ANSWER(StatusCode.ERROR, "答案不能为空"),
        INEXISTENCE_USER(StatusCode.INEXISTENCE_USER, "用户名不存在"),
        EXIST_USER(StatusCode.EXIST_USER, "用户名已存在"),
        EXIST_EMAIL(StatusCode.EXIST_EMAIL, "邮箱已注册"),
        EMPTY_PARAM(StatusCode.ERROR, "注册信息不能为空"),
        EMPTY_PARAM2(StatusCode.ERROR, "更新信息不能为空"),
        SUCCESS_USER(StatusCode.SUCESS, "用户注册成功"),
        NOSUCCESS_USER(StatusCode.ERROR, "用户注册失败"),
        SUCCESS_MSG(StatusCode.SUCESS, "校验成功,可以使用"),
        NO_LOGIN(StatusCode.NO_LOGIN, "用户未登录"),
        NO_QUESTION(StatusCode.NO_QUESTION, "该用户未设置找回密码问题"),
        ERROR_ANSWER(StatusCode.ERROR_ANSWER, "问题答案错误"),
        KONG_EFFICACY(StatusCode.ERROR, "token不能为空"),
        LOSE_EFFICACY(StatusCode.LOSE_EFFICACY, "token已经失效"),
        UNLAWFULNESS_TOKEN(StatusCode.UNLAWFULNESS_TOKEN, "非法的token"),
        DEFEACTED_PASSWORDNEW(StatusCode.DEFEACTED_PASSWORDNEW, "修改密码操作失败"),
        SUCCESS_PASSWORDNEW(StatusCode.SUCESS, "修改密码操作成功"),
        ERROR_PASSWORDOLD(StatusCode.ERROR_PASSWORDOLD, "旧密码输入错误"),
        SUCCESS_USERMSG(StatusCode.SUCESS, "更新个人信息成功"),
        NOSUCCESS_USERMSG(StatusCode.ERROR, "更新个人信息失败"),
        FORCE_EXIT(StatusCode.FORCE_EXIT, "用户未登录，无法获取当前用户信息"),
        LOGOUT(StatusCode.SUCESS, "退出成功");

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


    //用户身份状态码
    public enum RoleEnum {
        ROLE_ADMIN(0, "管理员"),
        ROLE_CUSTOMER(1, "普通用户");

        private int code;
        private String desc;

        private RoleEnum(int code, String desc) {
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

//商品状态码
    public enum ProductStatusEnum {

        PRODUCT_ONLINE(1, "在售"),
        PRODUCT_OFFLINE(2, "下架"),
        PRODUCT_DELETE(3, "删除"),
        ERROR_PAMAR(StatusCode.ERROR_PAMAR, "参数错误"),
        NO_PRODUCT(StatusCode.NO_PRODUCT, "该商品已下架");
        private int code;
        private String desc;

        private ProductStatusEnum(int code, String desc) {
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

//购物车状态码信息
    public enum CartCheckedEnum {

        EMPTY_PARAM(StatusCode.EMPTY_PARAM, "参数不能为空"),
        BZ_XQ(101, "商品详情页更新数量"),
        BZ_GWC(102, "购物车页面更新数量"),
        PRODUCT_CHECKED(1, "已勾选"),
        PRODUCT_UNCHECKED(0, "未勾选"),
        NO_SESSION(StatusCode.NO_SESSION, "用户未登录,请登录"),
        EMPTY_CART(StatusCode.EMPTY_CART, "还没有选中任何商品哦~"),
        FALSE_UPDATE(StatusCode.FALSE_UPDATE, "更新数据失败"),
        UNEXIST_P(StatusCode.UNEXIST_P, "商品不存在");

        private int code;
        private String desc;

        private CartCheckedEnum(int code, String desc) {
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

    //订单状态码信息
    public enum OrderStatusEnum {

        ORDER_zhifu(1, "线上支付"),//支付方式
        ORDER_POSTAGE(0, "邮费"),//邮费
        ORDER_CANCELED(0, "已取消"),
        ORDER_UN_PAY(10, "未付款"),
        ORDER_PAYED(20, "已付款"),
        ORDER_SEND(40, "已发货"),
        ORDER_SUCCESS(50, "交易成功"),
        ORDER_CLOSED(60, "交易关闭"),
        FALSE_CREAT(StatusCode.FALSE_CREAT, "创建订单失败"),
        ORDEREMPTY_PARAM(StatusCode.ORDEREMPTY_PARAM, "参数不能为空"),
        EMPTY_CARTS(StatusCode.EMPTY_CARTS, "还没有选中任何商品"),
        LACK_PRODUCT(StatusCode.LACK_PRODUCT, "商品库存不足"),
        NO_ORDERMSG(StatusCode.NO_ORDERMSG, "未查询到订单信息"),
        ACCOUNT_PAID(StatusCode.ACCOUNT_PAID, "订单不可取消"),
        NO_PAYORDER(StatusCode.NO_PAYORDER, "要支付的订单不存在"),
        BAD_PAYORDER(StatusCode.BAD_PAYORDER, "要支付的订单不合法");
        private int code;
        private String desc;

        private OrderStatusEnum(int code, String desc) {
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

//支付模块状态码信息
    public enum PaymentPlatformEnum {

        ALIPAY(1,"支付宝"),
        ALIPAY_FALSE(StatusCode.ALIPAY_FALSE,"支付宝预下单失败"),
        VERIFY_SIGNATURE_FALSE(StatusCode.VERIFY_SIGNATURE_FALSE,"支付宝验签失败"),
        VERIFY_ORDER_FALSE(StatusCode.VERIFY_ORDER_FALSE,"不是本商品的订单"),
        REPEAT_USEALIPAY(StatusCode.REPEAT_USEALIPAY,"支付宝重复调用"),
        SAVEPAYMSG_FALSE(StatusCode.SAVEPAYMSG_FALSE,"支付信息保存失败");


        private int code;
        private String desc;

        private PaymentPlatformEnum(int code, String desc) {
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
}

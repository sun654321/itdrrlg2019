package com.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.common.Const;
import com.common.ResponseCode;
import com.pojo.User;
import com.pojo.pay.Configs;
import com.service.OrderService;
import com.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@ResponseBody
@RequestMapping("/order/")
public class PayController {

    @Autowired
    PayService payService;

    //用户支付
    @RequestMapping("pay.do")
    public ResponseCode pay(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = payService.pay(orderNo, user.getId());
        return rs;
    }

    //支付宝回调
    @RequestMapping("alipay_callback.do")
    public String alipayCallback(HttpServletRequest request, HttpServletResponse response) {

        //获取支付宝返回的参数，返回一个map集合
        Map<String, String[]> map = request.getParameterMap();
        //获取上面集合键的set集合
        Set<String> strings = map.keySet();
        //获取该集合的迭代器
        Iterator<String> iterator = map.keySet().iterator();
        //创建一个新的map集合用于存储支付宝的数据，去除不必要的数据
        Map<String, String> params = new HashMap<>();

        //遍历原始集合，键的集合中数据
        while (iterator.hasNext()) {
            String next = iterator.next();
            String[] strings1 = map.get(next);
            StringBuffer ss = new StringBuffer("");

            for (int i = 0; i < strings1.length; i++) {
                //如果只有一个元素，就保存一个元素
                //有多个元素时，每个元素之间用逗号隔开
                ss = (i == strings1.length - 1) ? ss.append(strings1[i]) : ss.append(strings1[i] + ",");
            }
            //放到新的集合里
            params.put(next, ss.toString());
        }
        //支付宝验签，是不是支付宝发送的请求，避免重复请求
        try {
            //去除参数中的这个参数（官方提示不需要）
            params.remove("sign_type");
            //调用支付宝封装的方法进行验签操作，需要返回数据+公钥+编码集+类型定义
            boolean result = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8",
                    Configs.getSignType());
            if (!result) {
                //验签失败，返回错误信息
                return "{'msg':'验证失败'}";
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            //验签失败，返回错误信息
            return "{'msg':'验证失败'}";
        }
        //官方文档中还有很多需要验证的参数
        ResponseCode rs = payService.alipayCallback(params);
     //业务层处理返回对应的状态信息
            if (rs.isSuccess()) {
                return "SUCCESS";
            } else {
                return "FAILED";
            }
    }

    //查询订单支付状态
    @RequestMapping("query_order_pay_status.do")
    public  ResponseCode  queryOrderPayStatus(Long orderNo,HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = payService.queryOrderPayStatus(orderNo,user.getId());
           return rs;
    }
}

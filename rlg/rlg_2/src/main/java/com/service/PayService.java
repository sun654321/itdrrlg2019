package com.service;

import com.common.ResponseCode;

import java.util.Map;

public interface PayService {
     //用户支付
    ResponseCode pay(Long orderNo, Integer id);
      //支付宝回调
    ResponseCode alipayCallback(Map<String, String> params);
    //查询订单支付状态
    ResponseCode queryOrderPayStatus(Long orderNo, Integer uid);
}

package com.service;

import com.common.ResponseCode;

public interface OrderService {
    //创建订单
    ResponseCode create( Integer shippingId,Integer uid);

   //获取订单的商品信息
    ResponseCode getordercartproduct(Integer uid, Long orderNo);

    //订单列表
    ResponseCode selectAll(Integer uid, Integer pageSize, Integer pageNum);
    //取消订单
    ResponseCode cancelOne(Integer uid, Long orderNo);
}

package com.service;

import com.common.ResponseCode;

public interface OrderService {
    //订单列表
    ResponseCode selectAll(Integer id, String pageSize, String pageNum);
   //获取订单的商品信息
    ResponseCode getordercartproduct(Integer id);

   //创建订单
    ResponseCode create(Integer shippingId);
}

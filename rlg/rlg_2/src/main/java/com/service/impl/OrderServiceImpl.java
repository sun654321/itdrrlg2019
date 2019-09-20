package com.service.impl;

import com.common.ResponseCode;
import com.mappers.OrderItemMapper;
import com.pojo.OrderItem;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    //订单列表
    @Override
    public ResponseCode<OrderItem> selectAll(Integer uid, Integer pageSize, Integer pageNum) {

        List<OrderItem> li = orderItemMapper.selectAll(uid,pageSize, pageNum);
        if(li==null){
            return ResponseCode.notseccessRs(5,"未查询到订单信息");
        }
        return ResponseCode.seccessRs(0, li);
    }


    //获取订单的商品信息
    @Override
    public ResponseCode<OrderItemMapper> getordercartproduct(Integer uid) {
        List<OrderItemMapper> list = orderItemMapper.getordercartproduct(uid);
        if (list == null) {
            return ResponseCode.notseccessRs(30, "该用户没有订单");

        }
        return ResponseCode.seccessRs(list);
    }

    //创建订单
    @Override
    public ResponseCode create(Integer shippingId) {

        return null;
    }

    //用户支付
    @Override
    public ResponseCode pay(Long orderNo) {
        return null;
    }
}


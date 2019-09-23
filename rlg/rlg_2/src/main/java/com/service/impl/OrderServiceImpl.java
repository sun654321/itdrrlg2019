package com.service.impl;

import com.common.ResponseCode;
import com.mappers.CartMapper;
import com.mappers.OrderItemMapper;
import com.mappers.ProductMapper;
import com.mappers.ShippingMapper;
import com.pojo.Cart;
import com.pojo.Order;
import com.pojo.OrderItem;
import com.pojo.Product;
import com.service.OrderService;
import com.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    CartMapper cartMapper;

    //创建订单
    @Override
    public ResponseCode create(Integer uid, Integer shippingId) {
        if (shippingId == null) {
            return ResponseCode.notseccessRs(1, "收货的地址输入的参数有误");
        }
        //验证收货地址是否存在
        int i = shippingMapper.selectByproductId(shippingId);
        if (i <= 0) {
            return ResponseCode.notseccessRs(1, "收货的地址有误");
        }
        List<Cart> list = cartMapper.selectCheck(uid);

        if (list == null || list.size() == 0) {
            return ResponseCode.notseccessRs("该用户没有选中的商品");
        }
        Product product = new Product();
        //遍历每一个选中的商品
        for (Cart cart : list) {
            //查询商品的信息
            List<Product> products = productMapper.selectProductId(cart.getProductId());
            if (products == null || products.size() <= 0) {
                return ResponseCode.notseccessRs("没有该商品的信息");
            }
            for (Product products1 : products) {
                if (products1.getStatus() != 1) {
                    return ResponseCode.notseccessRs("商品已下架");
                }
                if (products1.getStock() > product.getStock()) {
                    return ResponseCode.notseccessRs("商品的库存不足");
                }
            }
        }
        return null;
    }

    //订单列表
    @Override
    public ResponseCode<OrderItem> selectAll(Integer uid, Integer pageSize, Integer pageNum) {

        List<OrderItem> li = orderItemMapper.selectAll(uid, pageSize, pageNum);
        if (li == null) {
            return ResponseCode.notseccessRs(5, "未查询到订单信息");
        }
        return ResponseCode.seccessRs(0, li);
    }


    //获取订单的商品信息
    @Override
    public ResponseCode<OrderItemMapper> getordercartproduct(Integer uid) {
        List<OrderItem> list = orderItemMapper.getordercartproduct(uid);
        if (list == null) {
            return ResponseCode.notseccessRs(30, "该用户没有订单");
        }
        return ResponseCode.seccessRs(list);
    }

}


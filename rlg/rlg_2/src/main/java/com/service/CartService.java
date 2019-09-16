package com.service;

import com.common.ResponseCode;

public interface CartService {
    //购物车List列表
    ResponseCode listdo(Integer id);
    //购物车添加商品
    ResponseCode add(Integer productId, Integer count, Integer integer);
    //更新购物车某个产品数量
    ResponseCode update(Integer uid, Integer productId, Integer count);
    //移除购物车某个产品
    ResponseCode deleteProduct(Integer id, String productIds);
    //购物车选中某个商品
    ResponseCode selectProduct(Integer id, Integer productId, Integer check);
    //购物车取消选中某个商品
    ResponseCode unselectProduct(Integer id, Integer productId, Integer check);
    //查询在购物车里的产品数量
    ResponseCode getcartproductcount(Integer id);
    //购物车全选
    ResponseCode select_all(Integer id);
}

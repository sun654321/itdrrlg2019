package com.service;

import com.common.ResponseCode;
import com.pojo.vo.CartVO;

public interface CartService {
    //购物车List列表
    ResponseCode<CartVO> listdo(Integer id);
    //购物车添加商品
    ResponseCode<CartVO> add(Integer productId, Integer count, Integer integer);
    //更新购物车某个产品数量
    ResponseCode<CartVO> update(Integer uid, Integer productId, Integer count);
    //移除购物车某个产品
    ResponseCode<CartVO> deleteProduct(Integer id, String productIds);
    //购物车选中某个商品
    ResponseCode<CartVO> selectProduct(Integer id, Integer productId, Integer check);
    //购物车取消选中某个商品
    ResponseCode<CartVO> unselectProduct(Integer id, Integer productId, Integer check);
    //查询在购物车里的产品数量
    ResponseCode<Integer> getcartproductcount(Integer id);
    //购物车全选
    ResponseCode<CartVO> selectall(Integer id);
    //购物车全取消
    ResponseCode<CartVO> unSelectAll(Integer id);
}

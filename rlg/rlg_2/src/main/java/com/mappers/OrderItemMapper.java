package com.mappers;

import com.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    //订单列表
    List<OrderItem> selectAll(@Param("uid")Integer uid,@Param("pageSize1") Integer pageSize1, @Param("pageNum1")Integer pageNum1);

    //获取订单的商品信息
    List<OrderItemMapper> getordercartproduct(Integer uid);

}
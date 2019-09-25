package com.mappers;

import com.pojo.Order;
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

    //获取订单的商品信息
    List<OrderItem> getordercartproduct(@Param("uid")Integer uid,@Param("orderNo")Long orderNo);
    //根据订单号查询对应商品详情
    List<OrderItem> selectByOrderNo(Long orderNo);
    //插入数据
    int insertAll(@Param("orderItem") List<OrderItem> orderItem);
//查询创建的数据
    List<OrderItem> selectByUidAndOrder(@Param("uid")Integer uid,@Param("orderNo") Long orderNo);
    //查询出商品的id
    Integer selectOne(@Param("uid")Integer uid, @Param("orderNo")Long orderNo);
}
package com.mappers;

import com.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);


    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    //插入一条数据
    int insert(Order record);

    //判断订单是否存在
    Order selectByOrderNo(Long orderNo);

    //判断订单和用户是否匹配
    Order selectByUidAndOrderNo(@Param("uid")Integer uid,@Param("orderNo")Long orderNo);

    int updateByPrimaryKey(Order record);

    //查询订单支付状态
    int queryOrderPayStatus(@Param("orderNo") Long orderNo,@Param("uid") Integer uid);
    //根据uid进行查询
    List<Order> selectByUid(Integer uid);
    //订单进行取消修改
    int updateUidAndOrderNo(@Param("uid")Integer uid, @Param("orderNo")Long orderNo);
}
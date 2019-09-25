package com.mappers;

import com.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int insert(Shipping record);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    //创建收货地址
    int createAddress(Shipping shipping);
    //修改收货地址的信息
    int updateAddress(Shipping shipping);
    //删除收货地址
    int deleteAddress(@Param("sid") Integer sid,@Param("uid") Integer uid );
    //根据uid查询该用户的收货地址
    List<Shipping> selectAddress(Integer uid);
   //估计收货的id进行查询
    Shipping selectByproductId(Integer shippingId);
    //查询某一个详细的收货地址信息
    Shipping selectOne(@Param("sid") Integer sid,@Param("uid") Integer uid);
}
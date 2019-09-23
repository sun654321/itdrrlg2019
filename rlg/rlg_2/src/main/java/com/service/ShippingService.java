package com.service;

import com.common.ResponseCode;
import com.pojo.Shipping;
import com.pojo.vo.ShippingVO;

public interface ShippingService {

    //创建收货地址
    ResponseCode<ShippingVO> createAddress(Shipping shipping, Integer uid);

    //修改收货地址
    ResponseCode updateAddress(Shipping shipping, Integer uid);

    //删除收货地址
    ResponseCode deleteAddress(Integer sid, Integer uid);
    //查询某一个详细的收货地址信息
    ResponseCode<Shipping> selectOne(Integer sid, Integer id);
}

package com.mappers;

import com.pojo.Payinfo;

public interface PayinfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Payinfo record);

    Payinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Payinfo record);

    int updateByPrimaryKey(Payinfo record);

    //支付宝查询
    int insert(Payinfo payinfo);
}
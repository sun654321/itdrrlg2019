package com.service;

import com.common.ResponseCode;

public interface CategroyService {
    //获取品类子节点
    ResponseCode selectone(Integer categroyId);
    //增加节点
    ResponseCode selectone1(Integer parentId, String categroyName);
    //修改品类姓名
    ResponseCode selectone2(Integer categroyId, String categroyName);
    //获取当前分类id及递归子节点categoryId
    ResponseCode selectone3(Integer categroyId);
//获取产品分类
    ResponseCode topcategory(Integer sid);
}

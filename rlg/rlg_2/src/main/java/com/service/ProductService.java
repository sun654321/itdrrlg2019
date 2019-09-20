package com.service;

import com.common.ResponseCode;


public interface ProductService {


    //产品搜索及动态排序List
    ResponseCode list(Integer categoryId, String keyword,Integer pageNum,
                      Integer pageSize,String orderBy);

    //产品detail
    ResponseCode detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner);

}

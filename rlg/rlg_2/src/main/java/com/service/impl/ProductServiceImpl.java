package com.service.impl;

import com.common.ResponseCode;
import com.mappers.ProductMapper;
import com.pojo.Categroy;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    //产品搜索及动态排序List
    @Override
    public ResponseCode list(Integer categoryId, String keyword, Integer pageNum,
                             Integer pageSize,String orderBy) {
        if (pageNum == null || pageNum.equals("")) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize.equals("")) {
            pageSize = 10;
        }
        List list = productMapper.list(categoryId, keyword, pageNum, pageSize);
        if (list == null) {
            return ResponseCode.notseccessRs("没有商品");
        }
        return ResponseCode.seccessRs(list);
    }

    //产品detail
    @Override
    public ResponseCode detail(Integer productId) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs("输入的id为空");
        }
        List list = productMapper.detail(productId);
        if (list == null) {
            return ResponseCode.notseccessRs("没有查到信息");
        }
        return ResponseCode.seccessRs(list);
    }


}

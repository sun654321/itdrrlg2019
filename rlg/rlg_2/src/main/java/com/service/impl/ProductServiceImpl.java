package com.service.impl;

import com.common.ResponseCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mappers.ProductMapper;
import com.pojo.Product;
import com.service.ProductService;
import com.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
     ProductMapper productMapper;

    //产品搜索及动态排序List
    @Override
    public ResponseCode list(Integer categoryId, String keyword, Integer pageNum,
                             Integer pageSize, String orderBy) {
//        if (categoryId == null || categoryId < 0) {
//            if (keyword == null || keyword.equals("")) {
//                return ResponseCode.notseccessRs("参数有误");
//            }
//        }
        //分割排序参数
        String[] split = new String[2];
        if (!orderBy.equals("")) {
            split = orderBy.split("_");
        }
        String key = "%" + keyword + "%";

        PageHelper.startPage(pageNum,pageSize,split[0] + " " + split[1]);
        List<Product> list = productMapper.list(categoryId, key, split[0], split[1]);
        PageInfo pf=new PageInfo(list);

        return ResponseCode.seccessRs(pf);
    }

    //产品detail
    @Override
    public ResponseCode detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner) {
        if (productId == null || productId<0) {
            return ResponseCode.notseccessRs("输入参数非法");
        }
        Product p = productMapper.detail(productId, is_new, is_hot, is_banner);
        if (p == null) {
            return ResponseCode.notseccessRs("商品不存在");
        }
        return ResponseCode.seccessRs(PoToVoUtil.productProductVO(p));
    }


}

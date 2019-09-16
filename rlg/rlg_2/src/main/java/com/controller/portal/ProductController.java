package com.controller.portal;

import com.common.ResponseCode;
import com.mappers.CategroyMapper;
import com.service.CategroyService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@ResponseBody
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
private CategroyService categroyService;
    //有问题
    //产品搜索及动态排序List
    @RequestMapping("list.do")
    public ResponseCode list(Integer categoryId,String keyword,Integer pageNum,
                             Integer pageSize,String orderBy){
        ResponseCode  rs=productService.list(categoryId,keyword, pageNum,pageSize,orderBy);
        return rs;
    }
    //产品detail
    @RequestMapping("detail.do")
    public ResponseCode detail(Integer productId){
        ResponseCode  rs=productService.detail(productId);
        return rs;
    }

//获取产品分类
    @RequestMapping("topcategory.do")
    public ResponseCode topcategory(Integer sid){
        ResponseCode  rs=categroyService.topcategory(sid);
        return rs;
    }

    //日志调用空接口
    @RequestMapping("logempty.do")
    public ResponseCode logempty(){
        ResponseCode  rs=null;
        return rs;
    }

}

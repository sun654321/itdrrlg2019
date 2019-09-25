package com.controller.portal;

import com.common.ResponseCode;
import com.pojo.Categroy;
import com.service.CategroyService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/product/")
public class ProductController {

    @Autowired
     ProductService productService;
    @Autowired
     CategroyService categroyService;
    //产品搜索及动态排序List
    @RequestMapping("list.do")
    public ResponseCode list(Integer categoryId,String keyword,
                             @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                             @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){
        ResponseCode  rs=productService.list(categoryId,keyword, pageNum,pageSize,orderBy);
        return rs;
    }

    //产品detail
    @RequestMapping("detail.do")
    public ResponseCode detail( Integer productId,
                                @RequestParam(value = "is_new",required = false,defaultValue = "0")Integer is_new ,
                                @RequestParam(value = "is_hot",required = false,defaultValue = "0") Integer is_hot,
                                @RequestParam(value = "is_banner",required = false,defaultValue = "0") Integer is_banner){
        ResponseCode  rs=productService.detail(productId,is_new,is_hot,is_banner);
        return rs;
    }

//获取产品分类
    @RequestMapping("topcategory.do")
    public ResponseCode<Categroy> topcategory(@RequestParam(value = "sid",required = false,defaultValue = "0")Integer sid){
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

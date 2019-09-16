package com.controller.backend;

import com.common.ResponseCode;
import com.service.CategroyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/manage/category/")
@ResponseBody
public class CategroyController {

    @Autowired
    private CategroyService categroyService;

    //获取品类子节点
    @RequestMapping("get_category.do")

    private ResponseCode getcategorydo(Integer categroyId) {
        ResponseCode rs = categroyService.selectone(categroyId);
        return rs;
    }

    //增加节点
    @RequestMapping("add_category.do")
    private ResponseCode addcategorydo(Integer parentId, String categroyName) {
        ResponseCode rs = categroyService.selectone1(parentId, categroyName);
        return rs;
    }

    //修改品类姓名
    @RequestMapping("set_category_name.do")
    private ResponseCode setcategorynamedo(Integer categroyId, String categroyName) {
        ResponseCode rs = categroyService.selectone2(categroyId, categroyName);
        return rs;
    }

    //获取当前分类id及递归子节点categoryId
    @RequestMapping("get_deep_category.do")
    private ResponseCode getdeepcategorydo(Integer categroyId) {
        ResponseCode rs = categroyService.selectone3(categroyId);
        return rs;
    }


}

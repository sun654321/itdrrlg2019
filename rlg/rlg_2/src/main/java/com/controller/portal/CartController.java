package com.controller.portal;


import com.common.Const;
import com.common.ResponseCode;
import com.pojo.Cart;
import com.pojo.User;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private CartService cartService;

    //购物车List列表
    @RequestMapping("list.do")
    public ResponseCode<Cart> listdo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.listdo(user.getId());
        return rs;
    }

    //购物车添加商品
    @RequestMapping("add.do")
    public ResponseCode<Cart> add(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.add(user.getId(), productId, count);
        return rs;
    }

    //更新购物车某个产品数量
    @RequestMapping("update.do")
    public ResponseCode<Cart> update(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.update(user.getId(), productId, count);
        return rs;
    }
    //移除购物车某个产品
    @RequestMapping("delete_product.do")
    public ResponseCode<Cart> deleteProduct( String productIds, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.deleteProduct(user.getId(), productIds);
        return rs;
    }

    //购物车选中某个商品
    @RequestMapping("select.do")
    public ResponseCode<Cart> selectProduct( Integer productId, HttpSession session,Integer check) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.selectProduct(user.getId(), productId,check);
        return rs;
    }

//购物车取消选中某个商品
    @RequestMapping("un_select.do")
    public ResponseCode<Cart> unselect( Integer productId, HttpSession session,Integer check) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.unselectProduct(user.getId(), productId,check);
        return rs;
    }

    //查询在购物车里的产品数量
    @RequestMapping("get_cart_product_count.do")
    public ResponseCode<Cart> getcartproductcount( HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.getcartproductcount(user.getId());
        return rs;
    }

    //购物车全选
    @RequestMapping("select_all.do")
    public ResponseCode<Cart> select_all( HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(10, "用户未登录,请登录");
        }
        ResponseCode rs = cartService.select_all(user.getId());
        return rs;
    }

}

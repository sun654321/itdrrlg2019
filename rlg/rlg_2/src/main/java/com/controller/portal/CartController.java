package com.controller.portal;


import com.common.Const;
import com.common.ResponseCode;
import com.pojo.Cart;
import com.pojo.User;
import com.pojo.vo.CartVO;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/cart/")
public class CartController {
    @Autowired
     CartService cartService;

    //购物车List列表
    @RequestMapping("list.do")
    public ResponseCode<CartVO> listdo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.listdo(user.getId());
        return rs;
    }

    //购物车添加商品
    @RequestMapping("add.do")
    public ResponseCode<CartVO> add(Integer productId,
                                  @RequestParam(value = "count", required = false, defaultValue = "1") Integer count,
                                  HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.add(user.getId(), productId, count);
        return rs;
    }

    //更新购物车某个产品数量
    @RequestMapping("update.do")
    public ResponseCode<CartVO> update(Integer productId, Integer count, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.update(user.getId(), productId, count);
        return rs;
    }

    //移除购物车某个产品
    @RequestMapping("delete_product.do")
    public ResponseCode<CartVO> deleteProduct(String productIds, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.deleteProduct(user.getId(), productIds);
        return rs;
    }

    //购物车选中某个商品
    @RequestMapping("select.do")
    public ResponseCode<CartVO> selectProduct(Integer productId, HttpSession session, Integer check) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.selectProduct(user.getId(), productId, check);
        return rs;
    }

    //购物车取消选中某个商品
    @RequestMapping("un_select.do")
    public ResponseCode<CartVO> unselect(Integer productId, HttpSession session, Integer check) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.unselectProduct(user.getId(), productId, check);
        return rs;
    }

    //查询在购物车里的产品数量
    @RequestMapping("get_cart_product_count.do")
    public ResponseCode<Integer> getcartproductcount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.getcartproductcount(user.getId());
        return rs;
    }

    //购物车全选
    @RequestMapping("select_all.do")
    public ResponseCode<CartVO> selectall(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.selectall(user.getId());
        return rs;
    }

    //购物车全取消
    @RequestMapping("un_select_all.do")
    public ResponseCode<CartVO> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = cartService.unSelectAll(user.getId());
        return rs;
    }
}

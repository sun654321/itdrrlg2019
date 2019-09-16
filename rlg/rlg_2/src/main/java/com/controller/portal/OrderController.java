package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.mappers.OrderItemMapper;
import com.pojo.OrderItem;
import com.pojo.User;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@ResponseBody
@RequestMapping("/order/")
public class OrderController {

   @Autowired
    private OrderService orderService;
    //订单列表
    @RequestMapping("list.do")
    public ResponseCode listdo(HttpSession session,String pageSize, String pageNum ) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs("用户未登录");
        }
        ResponseCode rs = orderService.selectAll(user.getId(),pageSize, pageNum);
        return rs;
    }

    //获取订单的商品信息
    @RequestMapping("get_order_cart_product.do")
    public ResponseCode<OrderItemMapper> getordercartproduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs("用户未登录");
        }
        ResponseCode rs = orderService.getordercartproduct(user.getId());
        return rs;

    }

//创建订单
    @RequestMapping("create.do")
    public ResponseCode<OrderItem> create(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs("用户未登录");
        }
        ResponseCode rs = orderService.create(shippingId);
        return rs;
    }

}

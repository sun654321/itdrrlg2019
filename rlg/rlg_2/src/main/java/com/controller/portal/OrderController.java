package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.User;
import com.pojo.vo.OrderListAndItemVO;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/order/")
public class OrderController {

   @Autowired
     OrderService orderService;


    //创建订单
    @RequestMapping("create.do")
    public ResponseCode create(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = orderService.create(shippingId,user.getId());
        return rs;
    }

    //获取订单的商品信息
    @RequestMapping("get_order_cart_product.do")
    public ResponseCode getordercartproduct(HttpSession session,
                                            @RequestParam(value ="orderNo" ,required = false) Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if (user == null) {
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = orderService.getordercartproduct(user.getId(),orderNo);
        return rs;
    }

        //订单列表
        @RequestMapping("list.do")
        public ResponseCode listdo(HttpSession session,
                @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer  pageSize,
                @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer  pageNum ) {
            User user = (User) session.getAttribute(Const.CURRENTUSER);
            if(user==null){
                return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
            }
            ResponseCode rs = orderService.selectAll(user.getId(),pageSize, pageNum);
            return rs;
        }


    //取消订单
    @RequestMapping("cancel.do")
    public ResponseCode cancelOne(HttpSession session,Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = orderService.cancelOne(user.getId(),orderNo);
        return rs;
    }
    }






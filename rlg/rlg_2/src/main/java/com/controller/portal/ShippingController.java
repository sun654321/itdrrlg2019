package com.controller.portal;

import com.common.Const;
import com.common.ResponseCode;
import com.pojo.Shipping;
import com.pojo.User;
import com.pojo.vo.ShippingVO;
import com.service.OrderService;
import com.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@ResponseBody
@RequestMapping("/order/")
public class ShippingController {

   @Autowired
   ShippingService shippingService;

    //创建收货地址的信息
    @RequestMapping("createAddress.do")
    public ResponseCode createAddress(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = shippingService.createAddress(shipping,user.getId());
        return rs;
    }

    //修改收货地址的信息
    @RequestMapping("updateAddress.do")
    public ResponseCode updateAddress(HttpSession session,Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = shippingService.updateAddress(shipping,user.getId());
        return rs;
    }
    //删除收货地址
    @RequestMapping("deleteAddress.do")
    public ResponseCode deleteAddress(HttpSession session,Integer sid) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = shippingService.deleteAddress(sid,user.getId());
        return rs;
    }

    //查询某一个详细的收货地址信息
    @RequestMapping("selectOne.do")
    public ResponseCode<Shipping> selectOne(HttpSession session,Integer sid) {
        User user = (User) session.getAttribute(Const.CURRENTUSER);
        if(user==null){
            return ResponseCode.notseccessRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }
        ResponseCode rs = shippingService.selectOne(sid,user.getId());
        return rs;
    }
}

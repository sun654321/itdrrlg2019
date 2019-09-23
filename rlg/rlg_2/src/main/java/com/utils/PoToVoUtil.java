package com.utils;

import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.common.Const;
import com.pojo.*;
import com.pojo.pay.BizContent;
import com.pojo.pay.Configs;
import com.pojo.pay.PGoodsDetail;
import com.pojo.vo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PoToVoUtil {

    //收货地址封装
    public static ShippingVO ShipVO(Shipping shipping) {
        ShippingVO shippingVO = new ShippingVO();
            shippingVO.setId(shipping.getId());
            shippingVO.setUserId(shipping.getUserId());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        return shippingVO;
    }


    //商品模块的产品detail
    public static ProductVO productProductVO(Product p) {
        ProductVO productVO = new ProductVO();
        productVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productVO.setId(p.getId());
        productVO.setCategoryId(p.getCategoryId());
        productVO.setName(p.getName());
        productVO.setSubtitle(p.getSubtitle());
        productVO.setMainImage(p.getMainImage());
        productVO.setPrice(p.getPrice());
        productVO.setStock(p.getStock());
        productVO.setStatus(p.getStatus());
        productVO.setIsNew(p.getIsNew());
        productVO.setIsHot(p.getIsHot());
        productVO.setIsBanner(p.getIsBanner());
        productVO.setCreateTime(p.getCreateTime());
        productVO.setUpdateTime(p.getUpdateTime());
        productVO.setSubImages(p.getSubImages());
        productVO.setDetail(p.getDetail());
        return productVO;
    }

    //添加购物车是返回的信息
    public static CartProductVO cartCartProductVO(Product product, Cart c) {
        CartProductVO cartProductVO = new CartProductVO();
        cartProductVO.setId(c.getId());
        cartProductVO.setUserId(c.getUserId());
        cartProductVO.setProductId(c.getProductId());
        cartProductVO.setProductChecked(c.getChecked());
        //查不到商品
        if (product != null) {
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductSubtitle(product.getSubtitle());
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductStatus(product.getStatus());
            cartProductVO.setProductStock(product.getStock());
        }

        //判断商品的库存
        Integer count = 0;
        if (c.getQuantity() <= product.getStock()) {

            count = c.getQuantity();
            cartProductVO.setLimitQuantity(Const.cart.LLIMITQUANTITYSUCCESS);
        } else {
            count = product.getStock();
            cartProductVO.setLimitQuantity(Const.cart.LLIMITQUANTITYNOT);
        }
        cartProductVO.setQuantity(count);

        //计算购物信息总价格
        BigDecimal pcart = BigDecimalUtils.mul(product.getPrice().doubleValue(), count);
        cartProductVO.setProductTotalPrice(pcart);

        return cartProductVO;

    }

    /*商品详情和支付宝商品类转换*/
    public static PGoodsDetail getNewPay(OrderItem orderItem) {
        PGoodsDetail info = new PGoodsDetail();
        info.setGoods_id(orderItem.getProductId().toString());
        info.setGoods_name(orderItem.getProductName());
        info.setPrice(orderItem.getCurrentUnitPrice().toString());
        info.setQuantity(orderItem.getQuantity().longValue());
        return info;
    }

    /*获取一个BizContent对象*/
    public static BizContent getBizContent(Order order, List<OrderItem> orderItems) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = String.valueOf(order.getOrderNo());

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "睿乐GO在线平台" + order.getPayment();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(order.getPayment());

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品" + orderItems.size() + "件共" + order.getPayment() + "元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "001";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "001";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem orderItem : orderItems) {
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = getNewPay(orderItem);
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        BizContent biz = new BizContent();
        biz.setSubject(subject);
        biz.setTotal_amount(totalAmount);
        biz.setOut_trade_no(outTradeNo);
        biz.setUndiscountable_amount(undiscountableAmount);
        biz.setSeller_id(sellerId);
        biz.setBody(body);
        biz.setOperator_id(operatorId);
        biz.setStore_id(storeId);
        biz.setExtend_params(extendParams);
        biz.setTimeout_express(timeoutExpress);
        //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
        //biz.setNotify_url(Configs.getNotifyUrl_test()+"portal/order/alipay_callback.do");
        biz.setGoods_detail(goodsDetailList);
        return biz;
    }

    //创建订单封装
    public static OrderVO orderOrderVO(Order order, OrderItem orderItem) {
        //封装OrderVO
        OrderVO orderVO = new OrderVO();

        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        orderVO.setPostage(order.getPostage());
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentTime(order.getPaymentTime());
        orderVO.setSendTime(order.getSendTime());
        orderVO.setEndTime(order.getEndTime());
        orderVO.setCloseTime(order.getCloseTime());
        orderVO.setCreateTime(order.getCreateTime());

        //封装OrderItemVo
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(orderItem.getCreateTime());


        orderVO.setOrderItemVoList(orderVO.getOrderItemVoList());

        orderVO.setShippingId(order.getShippingId());
        //有问题
        orderVO.setShippingVo("  ");

        return orderVO;

    }


}

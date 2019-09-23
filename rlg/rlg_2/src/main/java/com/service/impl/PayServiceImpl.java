package com.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.common.Const;
import com.common.ResponseCode;
import com.mappers.*;
import com.pojo.Order;
import com.pojo.OrderItem;
import com.pojo.Payinfo;
import com.pojo.pay.Configs;
import com.pojo.pay.ZxingUtils;
import com.service.PayService;
import com.utils.DateUtils;
import com.utils.JsonUtils;
import com.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
     OrderMapper orderMapper;
    @Autowired
     OrderItemMapper orderItemMapper;
    @Autowired
     PayinfoMapper payinfoMapper;

    //用户支付
    @Override
    public ResponseCode pay(Long orderNo, Integer uid) {
        //参数非空判断
        if (orderNo == null || orderNo <= 0) {
            return ResponseCode.notseccessRs("非法参数");
        }
        //判断订单是否存在
        Order order = orderMapper.selectByOrderNo(orderNo);

        if (order == null) {
            return ResponseCode.notseccessRs("订单不存在");
        }
        //判断订单和用户是否匹配
        Order order1 = orderMapper.selectByUidAndOrderNo(uid, orderNo);

        if (order1 == null) {
            return ResponseCode.notseccessRs("用户不匹配");
        }
        //根据订单号查询对应商品详情
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
        //调用支付宝接口获取支付二维码
        try {
            AlipayTradePrecreateResponse response = testTradePrecreate(order, orderItems);
            //响应成功才执行下一步
            if (response.isSuccess()) {
                //将二维码信息串生成图片，并保存，（需要修改为运行机器上的路径）
                String filePath = String.format(Configs.getSavecode_test() + "qr-%s.png",
                        response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                //预下单成功返回信息
                Map map = new HashMap();
                map.put("orderNo", order.getOrderNo());
                map.put("qrCode", filePath);
                return ResponseCode.seccessRs(map);
            } else {
                //预下单失败
                return ResponseCode.notseccessRs("下单失败");
            }
        } catch (AlipayApiException e) {
            //出现异常，预下单失败
            e.printStackTrace();
            return ResponseCode.notseccessRs("下单失败");
        }
        //后期图片会放到图片服务器上
    }


    //支付宝回调
    @Override
    public ResponseCode alipayCallback(Map<String, String> params) {
        //获取orderNo订单编号
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        //获取流水号
        String trade_no = params.get("trade_no");
        //获取支付状态
        String trade_status = params.get("trade_status");
        //获取支付时间
        String gmt_payment = params.get("gmt_payment");
        //获取金额
        BigDecimal total_amount = new BigDecimal(params.get("total_amount"));

        //验证订单是否正确
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            return ResponseCode.notseccessRs("订单错误");
        }
        //验证金额
        if (!total_amount.equals(order.getPayment())) {
            return ResponseCode.notseccessRs("金额不匹配");
        }
        //判断支付的状态，防止支付宝重复回调
        if (order.getStatus() != 10) {
            return ResponseCode.notseccessRs("不是付款状态");
        }
        //校验状态码，支付成功
        if (trade_status.equals(Const.TRADE_SUCCESS)) {
            //更改数据库中订单的状态+更改支付时间+更新时间+删除用过的本地二维码
            order.setStatus(20);
            order.setPaymentTime(DateUtils.strToDate(gmt_payment));

            //进行修改订单的状态码，支付时间，修改时间
            orderMapper.updateByPrimaryKey(order);

            //支付成功，删除本地存在的二维码图片
            String str = String.format(Configs.getSavecode_test() + "qr-%s.png",
                    order.getOrderNo());
            File file = new File(str);
            boolean b = file.delete();
            if (b != true) {
                System.out.println("删除成功");
            }
            System.out.println("删除失败");
        }

        //保存支付宝支付信息
        Payinfo payinfo = new Payinfo();
        payinfo.setOrderNo(orderNo);
        payinfo.setPayPlatform(Const.PaymentPlatformEnum.ALIPAY.getCode());
        payinfo.setPlatformStatus(trade_status);
        payinfo.setPlatformNumber(trade_no);
        payinfo.setUserId(order.getUserId());
        //进行生成支付订单
        int i = payinfoMapper.insert(payinfo);
        if (i > 0) {
            //支付信息保存成功返回结果SUCCESS，让支付宝不再回调
            return ResponseCode.seccessRs("SUCCESS");
        }
        //支付信息保存失败返回结果
        return ResponseCode.notseccessRs("FAILED");
    }


    //查询订单支付状态
    @Override
    public ResponseCode queryOrderPayStatus(Long orderNo, Integer uid) {
        if (orderNo == null || orderNo <= 0) {
            return ResponseCode.notseccessRs("非法参数");
        }
        int i = orderMapper.queryOrderPayStatus(orderNo, uid);
        if (i <= 10) {
            return  ResponseCode.notseccessRs(1,"false");
        }
        return ResponseCode.notseccessRs(0,"true");
    }



    private AlipayTradePrecreateResponse testTradePrecreate(Order order, List<OrderItem> orderItems)
            throws AlipayApiException {
        //读取配置文件
        Configs.init("zfbinfo.properties");
        //实例化支付宝客户端
        AlipayClient alipayClient = new DefaultAlipayClient(Configs.getOpenApiDomain(),
                Configs.getAppid(), Configs.getPrivateKey(),
                "json", "utf-8",
                Configs.getAlipayPublicKey(),
                Configs.getSignType());
        //创建API对应的request类
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        //获取一个BizContext对象，并转换成json格式
        String str = JsonUtils.obj2String(PoToVoUtil.getBizContent(order, orderItems));
        request.setBizContent(str);

        //设置支付宝回调路径
        request.setNotifyUrl(Configs.getNotifyUrl_test());
        //获取响应,这里要处理一下异常
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        //返回响应的结果
        return response;
    }
}


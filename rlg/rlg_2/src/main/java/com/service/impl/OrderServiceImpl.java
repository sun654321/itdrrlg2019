package com.service.impl;

import com.common.Const;
import com.common.ResponseCode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mappers.*;
import com.pojo.*;
import com.pojo.vo.*;
import com.service.OrderService;
import com.utils.BigDecimalUtils;
import com.utils.PoToVoUtil;
import com.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    CartMapper cartMapper;
    @Autowired
    OrderMapper orderMapper;

    //创建订单
    @Override
    public ResponseCode create(Integer shippingId, Integer uid) {
        if (shippingId == null) {
            return ResponseCode.notseccessRs(1, "收货的地址输入的参数有误");
        }

        //验证收货地址是否存在
        Shipping shipping = shippingMapper.selectByproductId(shippingId);
        if (shipping == null) {
            return ResponseCode.notseccessRs(1, "收货的地址有误");
        }
        //
        List<Product> productList = new ArrayList<>();
        //获取用户购物车中选中的商品信息
        List<Cart> list = cartMapper.selectCheck(uid);

        if (list.size() == 0) {
            return ResponseCode.notseccessRs("该用户没有选中商品");
        }
        //计算订单总价
        BigDecimal payment = new BigDecimal("0");
        //遍历每一个选中的商品
        for (Cart cart : list) {
            //查询商品的信息
            Product products = productMapper.selectProductId(cart.getProductId());
            if (products == null) {
                return ResponseCode.notseccessRs("没有该商品的信息");
            }
            if (products.getStatus() != 1) {
                return ResponseCode.notseccessRs(products.getName() + "商品已下架");
            }
            if (cart.getQuantity() > products.getStock()) {
                return ResponseCode.notseccessRs(products.getName() + "商品的库存不足");
            }

            //根据购物车购物数量和商品单价计算一条购物车信息的总价
            BigDecimal mul = BigDecimalUtils.mul(products.getPrice().doubleValue(), cart.getQuantity());

            //计算商品的总价格
            payment = BigDecimalUtils.add(payment.doubleValue(), mul.doubleValue());

            productList.add(products);

        }
        //创建订单,没有问题存储到数据库中
        Order order = this.getOrder(uid, shippingId);
        order.setPayment(payment);

        int insert = orderMapper.insert(order);
        if (insert <= 0) {
            return ResponseCode.notseccessRs(order.getOrderNo() + "订单创建失败");
        }

        //创建订单详情，没有问题存储到数据库中，使用批量插入的方式
        List<OrderItem> orderItem = this.getOrderItem(uid, order.getOrderNo(), productList, list);
        int i1 = orderItemMapper.insertAll(orderItem);
        if (i1 <= 0) {
            return ResponseCode.notseccessRs(order.getOrderNo() + "订单详情创建失败");
        }


        //插入成功,减少库存
        for (OrderItem item : orderItem) {
            for (Product product : productList) {
                if (item.getProductId() == product.getId()) {
                    Integer count = product.getStock() - item.getQuantity();
                    if (count < 0) {
                        return ResponseCode.notseccessRs("库存不能为负数");
                    }
                    product.setStock(count);
                    //更新数据信息到数据库
                    int updateId = productMapper.updateId(item.getProductId(), product.getStock());
                    if (updateId <= 0) {
                        return ResponseCode.notseccessRs("库存更新失败");
                    }
                }
            }
        }
        //清空购物车
        int cartDelect = cartMapper.delectuid(list, uid);
        if (cartDelect <= 0) {
            return ResponseCode.notseccessRs("清空购物车失败");
        }

        //查询OrderItem创建的数据
        List<OrderItem> orderItemList = orderItemMapper.selectByUidAndOrder(uid, order.getOrderNo());

        //拼接数据，返回vo类
        List<OrderItemVO> orderItemVos = new ArrayList<>();
        for (OrderItem item : orderItemList) {
            OrderItemVO orderItemVo = PoToVoUtil.orderItemVOTo(item);
            orderItemVos.add(orderItemVo);
        }
        //根据订单号和uid查询order的信息
        Order order1 = orderMapper.selectByUidAndOrderNo(uid, order.getOrderNo());

        ShippingVO2 shippingVO2 = PoToVoUtil.slshippingVO2(shipping);

        OrderVO orderVO = PoToVoUtil.orderOrderVO(order1, shippingVO2, orderItemVos);

        return ResponseCode.seccessRs(orderVO);
    }

    //创建订单对象
    private Order getOrder(Integer uid, Integer shipping) {
        Order order = new Order();
        order.setUserId(uid);
        order.setOrderNo(this.getOrderNo());
        order.setShippingId(shipping);

        //需要维护
        order.setPaymentType(Const.OrderStatusEnum.ORDER_zhifu.getCode());
        order.setPostage(Const.OrderStatusEnum.ORDER_POSTAGE.getCode());
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());

        return order;
    }

    //创建一个订单详情对象
    private List<OrderItem> getOrderItem(Integer uid, Long orderNo, List<Product> list, List<Cart> cartList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cart.getQuantity());
            for (Product product : list) {
                if (product.getId().equals(cart.getProductId())) {
                    orderItem.setUserId(uid);
                    orderItem.setOrderNo(orderNo);
                    orderItem.setProductId(product.getId());
                    orderItem.setProductName(product.getName());
                    orderItem.setProductImage(product.getMainImage());
                    orderItem.setCurrentUnitPrice(product.getPrice());
                    BigDecimal mul = BigDecimalUtils.mul(product.getPrice().doubleValue(), cart.getQuantity());
                    orderItem.setTotalPrice(mul);
                    orderItemList.add(orderItem);
                }
            }
        }
        return orderItemList;
    }


    //计算订单总价
//    private BigDecimal  getPayMent(){
//
// }

    //生成订单编号
    private Long getOrderNo() {
        Long l = System.currentTimeMillis();
        Long orderNo = l + Math.round(Math.random() * 100);
        return orderNo;
    }


    //获取订单的商品信息
    @Override
    public ResponseCode getordercartproduct(Integer uid, Long orderNo) {

        //订单号不为空
        if (orderNo != null) {
            List<OrderItem> orderItemList = orderItemMapper.getordercartproduct(uid, orderNo);
            Order order = orderMapper.selectByOrderNo(orderNo);
            if (order == null) {
                return ResponseCode.notseccessRs("没有订单");
            }
            if (orderItemList == null || orderItemList.size() <= 0) {
                return ResponseCode.notseccessRs("该用户没有订单");
            }

            List<OrderItemVO> itemVOList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList) {
                OrderItemVO orderItemVO = PoToVoUtil.orderItemVOTo(orderItem);
                itemVOList.add(orderItemVO);
            }
            OrderXxDingDanVO orderXxDingDanVO = new OrderXxDingDanVO();
            orderXxDingDanVO.setOrderItemVoList(itemVOList);
            orderXxDingDanVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
            orderXxDingDanVO.setProductTotalPrice(order.getPayment());
            return ResponseCode.seccessRs(orderXxDingDanVO);
        } else {
            //订单号为空
            //获取用户购物车中选中的商品信息
            List<Cart> list = cartMapper.selectCheck(uid);
            if (list.size() == 0) {
                return ResponseCode.notseccessRs("该用户没有选中商品");
            }
            //计算订单总价
            BigDecimal payment = new BigDecimal("0");
            List<Product> productList = new ArrayList<>();
            //遍历每一个选中的商品
            for (Cart cart : list) {
                //查询商品的信息
                Product products = productMapper.selectProductId(cart.getProductId());
                if (products == null) {
                    return ResponseCode.notseccessRs("没有该商品的信息");
                }
                if (products.getStatus() != 1) {
                    return ResponseCode.notseccessRs(products.getName() + "商品已下架");
                }
                if (cart.getQuantity() > products.getStock()) {
                    return ResponseCode.notseccessRs(products.getName() + "商品的库存不足");
                }

                //根据购物车购物数量和商品单价计算一条购物车信息的总价
                BigDecimal mul = BigDecimalUtils.mul(products.getPrice().doubleValue(), cart.getQuantity());

                //计算商品的总价格
                payment = BigDecimalUtils.add(payment.doubleValue(), mul.doubleValue());
                productList.add(products);
            }
            List<OrderItem> orderItemList = this.getOrderItem(uid, null, productList, list);
            List<OrderItemVO> itemVOList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList) {
                OrderItemVO orderItemVO = PoToVoUtil.orderItemVOTo(orderItem);
                itemVOList.add(orderItemVO);
            }
            OrderXxDingDanVO orderXxDingDanVO = new OrderXxDingDanVO();
            orderXxDingDanVO.setOrderItemVoList(itemVOList);
            orderXxDingDanVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
            orderXxDingDanVO.setProductTotalPrice(payment);
            return ResponseCode.seccessRs(orderXxDingDanVO);
        }
    }

    //订单列表
    @Override
    public ResponseCode selectAll(Integer uid, Integer pageSize, Integer pageNum) {
        //根据uid进行Order查询
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);
        //创建集合把Order数据放到集合里
        List<OrderListAndItemVO> orderListVOS = new ArrayList<>();
        if (orderList == null || orderList.size() <= 0) {
            return ResponseCode.notseccessRs(5, "未查询到订单信息");
        }
        for (Order order : orderList) {
            //根据uid进行OrderItem查询
            List<OrderItem> orderItemList = orderItemMapper.selectByUidAndOrder(uid, order.getOrderNo());
            if (orderItemList == null || orderItemList.size() <= 0) {
                return ResponseCode.notseccessRs(5, "未查询到订单详细信息");
            }
            //根据uid进行shipping查询
            List<Shipping> shippings = shippingMapper.selectAddress(uid);
            if (shippings == null || shippings.size() <= 0) {
                return ResponseCode.notseccessRs(5, "未查询到地址信息");
            }
            for (Shipping shipping : shippings) {
                if (order.getShippingId().equals(shipping.getId())) {
                    ShippingVO2 shippingVO2 = PoToVoUtil.slshippingVO2(shipping);
                    OrderListAndItemVO listAndItemVO = this.orderLbVO(order, shippingVO2, orderItemList);
                    orderListVOS.add(listAndItemVO);
                }
            }
        }

        PageInfo pf = new PageInfo(orderList);
        pf.setList(orderListVOS);

        return ResponseCode.seccessRs(pf);
    }

    //列表封装
    private OrderListAndItemVO orderLbVO(Order order, ShippingVO2 shippingVO2, List<OrderItem> orderItemList) {
        //封装OrderVO
        OrderListAndItemVO orderListAndItemVO = new OrderListAndItemVO();

        orderListAndItemVO.setOrderNo(order.getOrderNo());
        orderListAndItemVO.setPayment(order.getPayment());
        orderListAndItemVO.setPaymentType(order.getPaymentType());
        orderListAndItemVO.setPostage(order.getPostage());
        orderListAndItemVO.setStatus(order.getStatus());
        orderListAndItemVO.setPaymentTime(order.getPaymentTime());
        orderListAndItemVO.setSendTime(order.getSendTime());
        orderListAndItemVO.setEndTime(order.getEndTime());
        orderListAndItemVO.setCloseTime(order.getCloseTime());
        orderListAndItemVO.setCreateTime(order.getCreateTime());

        //创建集合把OrderItemVO数据放到集合里
        List<OrderItemVO> itemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            if (orderItem.getOrderNo().equals(order.getOrderNo())) {
                OrderItemVO orderItemVO = PoToVoUtil.orderItemVOTo(orderItem);
                itemVOList.add(orderItemVO);
            }
        }
        orderListAndItemVO.setOrderItemVoList(itemVOList);
        orderListAndItemVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        orderListAndItemVO.setShippingId(order.getShippingId());
        orderListAndItemVO.setReceiverName(shippingVO2.getReceiverName());
        orderListAndItemVO.setShippingVO2(shippingVO2);
        return orderListAndItemVO;
    }

    //取消订单
    @Override
    public ResponseCode cancelOne(Integer uid, Long orderNo) {
        if (orderNo == null || orderNo <= 0) {
            return ResponseCode.notseccessRs("订单号非法参数");
        }
        Order order = orderMapper.selectByUidAndOrderNo(uid, orderNo);
        if (order == null) {
            return ResponseCode.notseccessRs(orderNo + "订单号不存在");
        }
        if (order.getStatus() != 10) {
            return ResponseCode.notseccessRs(orderNo + "不可进行取消订单");
        }
        //订单进行取消修改
        int i = orderMapper.updateUidAndOrderNo(uid, orderNo);
        if (i <= 0) {
            return ResponseCode.notseccessRs(orderNo + "取消订单失败");
        }
        //取消库存锁定
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectProductId(orderItem.getProductId());
            product.setStock(product.getStock() + orderItem.getQuantity());
            int i1 = productMapper.updateId(orderItem.getProductId(), product.getStock());
            if (i1 <= 0) {
                return ResponseCode.seccessRs(orderNo + "库存回滚取消失败");
            }
        }
        return ResponseCode.seccessRs(orderNo + "订单取消成功");
    }
}


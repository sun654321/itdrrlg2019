package com.service.impl;

import com.common.Const;
import com.common.ResponseCode;
import com.google.common.collect.Lists;
import com.mappers.CartMapper;
import com.mappers.ProductMapper;
import com.pojo.Cart;
import com.pojo.Product;
import com.pojo.vo.CartProductVO;
import com.pojo.vo.CartVO;
import com.service.CartService;
import com.utils.BigDecimalUtils;
import com.utils.PoToVoUtil;
import com.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    //购物车List列表
    @Override
    public ResponseCode listdo(Integer id) {
        CartVO cartVO = this.cartList(id);
        return ResponseCode.seccessRs(cartVO);
    }

    //购物车添加商品
    @Override
    public ResponseCode<CartVO> add(Integer uid, Integer productId, Integer count) {
        if (productId == null || productId <= 0) {
            return ResponseCode.notseccessRs("商品id为空");
        }
        if (count == null || count <= 0) {
            return ResponseCode.notseccessRs("参数错误");
        }
        Product product = productMapper.selectByproductId(productId);
        if (product == null) {
            return ResponseCode.notseccessRs("该商品不存在");
        }
        Cart cart = cartMapper.selectProductIdUid(uid, productId);
        Cart cart1 = new Cart();
        if (cart == null) {
            //添加购物信息
            cart1.setUserId(uid);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            int add = cartMapper.add(cart1);
            if (add <= 0) {
                return ResponseCode.notseccessRs("添加失败");
            }
        }
        //已经存在更新产品
        else {
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(uid);
            cart1.setQuantity(cart.getQuantity() + count);
            cart1.setChecked(cart.getChecked());
            int row = cartMapper.updatecart(cart1);
            if (row <= 0) {
                return ResponseCode.notseccessRs("更新失败");
            }

        }
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);
    }

    //返回购物车信息
    private CartVO cartList(Integer uid) {
        //创建对象
        CartVO cartVO = new CartVO();
        //创建变量存储购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //创建存储对象的集合
        List<CartProductVO> cartProductVOList = new ArrayList<CartProductVO>();
        //根据id进行查询
        List<Cart> list = cartMapper.listdo(uid);
        if (list.size() != 0) {
            for (Cart cart : list) {
                //查询商品信息
                Product product = productMapper.selectByproductId(cart.getProductId());
                //使用工具类进行数据封装
                CartProductVO cartProductVO = PoToVoUtil.cartCartProductVO(product, cart);
                //购物车更新有效信息
                Cart cartForQuantity=new Cart();
                cartForQuantity.setId(cart.getId());
                cartForQuantity.setQuantity(cartProductVO.getQuantity());
                cartMapper.updatecartQuantity(cartForQuantity);


                //计算购物车总价
                if (cart.getChecked() == Const.cart.CHECK) {
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                //存储到集合里
                cartProductVOList.add(cartProductVO);
            }
        }
        //封装CartVO
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setIsallchecked(this.checkAll(uid));
        cartVO.setCarttotalprice(cartTotalPrice);
        cartVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        return cartVO;
    }

    //判断用户购物车是否全选
    private boolean checkAll(Integer uid) {
        int i = cartMapper.selectByUidCheck(uid, Const.cart.UNCHECK);
        if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

    //更新购物车某个产品数量
    @Override
    public ResponseCode<CartVO> update(Integer uid, Integer productId, Integer count) {
        if (productId == null || productId <= 0) {
            return ResponseCode.notseccessRs(10, "非法参数");
        }
        if (count == null || count <= 0) {
            return ResponseCode.notseccessRs(10, "非法参数");
        }
        Cart cart = cartMapper.selectProductIdUid(uid, productId);
        cart.setQuantity(count);
        int i = cartMapper.updatecart(cart);
        if (i <= 0) {
            return ResponseCode.notseccessRs("更新数据失败");
        }
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);
    }

    //移除购物车某个产品
    @Override
    public ResponseCode<CartVO> deleteProduct(Integer uid, String productIds) {
        if (productIds == null || productIds.equals("")) {
            return ResponseCode.notseccessRs(10, " 非法参数");
        }
//        //创建集合存储商品id
//        List<Integer> productIdList = Lists.newArrayList();
//        //进行分割
//        String[] productIdsArr = productIds.split(",");
//        if (productIdsArr != null && productIdsArr.length > 0) {
//            for (String productIdstr : productIdsArr) {
//                Integer productId = Integer.parseInt(productIdstr);
//                productIdList.add(productId);
//            }
//        }
        //第二种方法

        String[] split = productIds.split(",");
        List<String> productIdList = Arrays.asList(split);

        int i = cartMapper.deleteProduct(uid, productIdList);
        if (i <= 0) {
            return ResponseCode.notseccessRs(10, "商品不存在");
        }
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);
    }


    //购物车选中某个商品
    @Override
    public ResponseCode<CartVO> selectProduct(Integer uid, Integer productId, Integer check) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs(10, " 参数不能为空");
        }
        if (check == null || check.equals("")) {
            check = 1;
        }
       //验证是否存在该商品
        Cart cart1 = cartMapper.selectProductIdUid(uid, productId);
        if (cart1==null){
            return ResponseCode.notseccessRs(3,"商品不存在");
        }
        //修改状态
        int cart = cartMapper.selectProduct(uid, productId, check);
        if (cart <= 0) {
            return ResponseCode.notseccessRs("失败");
        }
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);
    }

    //购物车取消选中某个商品
    @Override
    public ResponseCode<CartVO> unselectProduct(Integer uid, Integer productId, Integer check) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs(10, " 参数不能为空");
        }
        if (check == null || check.equals("")) {
            check = 0;
        }
        //验证是否存在该商品
        Cart cart1 = cartMapper.selectProductIdUid(uid, productId);
        if (cart1==null){
            return ResponseCode.notseccessRs(3,"商品不存在");
        }
        //修改状态
        int cart = cartMapper.unselectProduct(uid, productId, check);
        if (cart <= 0) {
            return ResponseCode.notseccessRs("失败");
        }
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);

    }

    //查询在购物车里的产品数量
    @Override
    public ResponseCode<Integer> getcartproductcount(Integer uid) {
        int i = cartMapper.getcartproductcount(uid);
        return ResponseCode.seccessRs(i);
    }


    //购物车全选
    @Override
    public ResponseCode<CartVO> selectall(Integer uid) {
        List<Cart> list = cartMapper.listdo(uid);

        if(list==null){
            return  ResponseCode.notseccessRs(3,"商品不存在");
        }
        cartMapper.updateByUidCheck(uid, Const.cart.CHECK);

        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);
    }

    //购物车全取消
    @Override
    public ResponseCode<CartVO> unSelectAll(Integer uid) {
        List<Cart> list = cartMapper.listdo(uid);

        if(list==null){
            return  ResponseCode.notseccessRs(3,"商品不存在");
        }
       cartMapper.updateByUidCheck(uid, Const.cart.UNCHECK);
        CartVO cartVO = cartList(uid);
        return ResponseCode.seccessRs(cartVO);

    }

}

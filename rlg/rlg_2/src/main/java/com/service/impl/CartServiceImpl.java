package com.service.impl;

import com.common.Const;
import com.common.ResponseCode;
import com.google.common.collect.Lists;
import com.mappers.CartMapper;
import com.mappers.ProductMapper;
import com.pojo.Cart;
import com.pojo.Product;
import com.service.CartService;
import com.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Cart> list = cartMapper.listdo(id);
        if (list == null) {
            return ResponseCode.notseccessRs(1, "还没有选中任何商品哦");
        }
        return ResponseCode.seccessRs(list);
    }

    //购物车添加商品
    @Override
    public ResponseCode add(Integer uid, Integer productId, Integer count) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs("商品id为空");
        }
        Product product = productMapper.selectByproductId(productId);
        if (product == null) {
            return ResponseCode.notseccessRs("该商品不存在");
        }
        Cart cart = cartMapper.selectProductIdUid(uid, productId);
        if (cart == null) {
            //添加购物信息
            Cart cart1 = new Cart();
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
            Cart cart1 = new Cart();
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
        return ResponseCode.seccessRs("成功");
    }

    //更新购物车某个产品数量
    @Override
    public ResponseCode update(Integer uid, Integer productId, Integer count) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs(9, "商品的id输入为空");
        }
        if (count == null || count.equals("")) {
            return ResponseCode.notseccessRs(9, "商品的数量输入有误");
        }
        Cart cart = cartMapper.selectProductIdUid(uid, productId);
        if (cart == null) {
            return ResponseCode.notseccessRs("该商品不存在");
        }
        cart.setQuantity(count);
        int i = cartMapper.updatecart(cart);

        if (i <= 0) {
            return ResponseCode.notseccessRs(2, "更新数据失败");
        }
        return ResponseCode.seccessRs("更新成功");
    }

    //移除购物车某个产品
    @Override
    public ResponseCode deleteProduct(Integer uid, String productIds) {
        if (productIds == null || productIds.equals("")) {
            return ResponseCode.notseccessRs(9, " 参数不能为空");
        }
        //创建集合存储商品id
        List<Integer> productIdList = Lists.newArrayList();
        //进行分割
        String[] productIdsArr = productIds.split(",");
        if (productIdsArr != null && productIdsArr.length > 0) {
            for (String productIdstr : productIdsArr) {
                Integer productId = Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }
        int i = cartMapper.deleteProduct(uid, productIdList);
        if (i <= 0) {
            return ResponseCode.notseccessRs(3, "商品不存在");
        }
        return ResponseCode.seccessRs("成功");
    }

    //购物车选中某个商品
    @Override
    public ResponseCode selectProduct(Integer uid, Integer productId, Integer check) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs(9, " 参数不能为空");
        }
        if (check == null || check.equals("")) {
            check = 1;
        }
        int cart = cartMapper.selectProduct(uid, productId, check);
        if (cart <= 0) {
            return ResponseCode.notseccessRs("修改失败");
        }
        return ResponseCode.seccessRs("成功");
    }

    //购物车取消选中某个商品
    @Override
    public ResponseCode unselectProduct(Integer uid, Integer productId, Integer check) {
        if (productId == null || productId.equals("")) {
            return ResponseCode.notseccessRs(9, " 参数不能为空");
        }
        if (check == null || check.equals("")) {
            check = 0;
        }
        int cart = cartMapper.unselectProduct(uid, productId, check);
        if (cart <= 0) {
            return ResponseCode.notseccessRs("修改失败");
        }
        return ResponseCode.seccessRs("成功");

    }

    //查询在购物车里的产品数量
    @Override
    public ResponseCode getcartproductcount(Integer uid) {
        int i = cartMapper.getcartproductcount(uid);
        return ResponseCode.seccessRs(i);
    }

   //购物车全选
    @Override
    public ResponseCode select_all(Integer id) {

        return null;
    }


}

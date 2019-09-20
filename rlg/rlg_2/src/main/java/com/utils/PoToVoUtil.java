package com.utils;

import com.common.Const;
import com.pojo.Cart;
import com.pojo.Product;
import com.pojo.vo.CartProductVO;
import com.pojo.vo.ProductVO;

import java.math.BigDecimal;

public class PoToVoUtil {
    //商品模块的产品detail
    public static ProductVO productProductVO(Product p){
        ProductVO productVO=new ProductVO();
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
    public static CartProductVO cartCartProductVO(Product product, Cart c){
        CartProductVO cartProductVO=new CartProductVO();
        cartProductVO.setId(c.getId());
        cartProductVO.setUserId(c.getUserId());
        cartProductVO.setProductId(c.getProductId());
        cartProductVO.setProductChecked(c.getChecked());
       //查不到商品
        if(product!=null){
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductSubtitle(product.getSubtitle());
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductStatus(product.getStatus());
            cartProductVO.setProductStock(product.getStock());
        }

        //判断商品的库存
        Integer count=0;
        if(c.getQuantity()<= product.getStock()){

            count=c.getQuantity();
            cartProductVO.setLimitQuantity(Const.cart.LLIMITQUANTITYSUCCESS);
        }else{
            count=product.getStock();
            cartProductVO.setLimitQuantity(Const.cart.LLIMITQUANTITYNOT);
        }
          cartProductVO.setQuantity(count);

       //计算购物信息总价格
        BigDecimal pcart=BigDecimalUtils.mul(product.getPrice().doubleValue(),count);
        cartProductVO.setProductTotalPrice(pcart);

        return cartProductVO;

    }


}

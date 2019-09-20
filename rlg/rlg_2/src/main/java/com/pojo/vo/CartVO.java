package com.pojo.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartVO  implements Serializable{
    //购物信息集合
    private List<CartProductVO> cartProductVOList;
    //是否全选
    private boolean isallchecked;
    //总价格
    private BigDecimal carttotalprice;

    private String imageHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public boolean isIsallchecked() {
        return isallchecked;
    }

    public void setIsallchecked(boolean isallchecked) {
        this.isallchecked = isallchecked;
    }

    public BigDecimal getCarttotalprice() {
        return carttotalprice;
    }

    public void setCarttotalprice(BigDecimal carttotalprice) {
        this.carttotalprice = carttotalprice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}

package com.mappers;


import com.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);


    //根据id进行查询验证
    List<Cart> listdo(Integer id);
    //根据用户名和商品id进行查询购物车信息
    Cart selectProductIdUid(@Param("uid")Integer uid,@Param("productId") Integer productId);
    //添加商品
    int add(Cart cart);
    //更新商品//更新购物车某个产品数量
    int updatecart(Cart cart1);
    //移除购物车某个产品
    int deleteProduct(@Param("uid")Integer uid,@Param("productIdList") List<String> productIdList);
   //购物车选中某个商品
    int selectProduct(@Param("uid")Integer uid, @Param("productId")Integer productId, @Param("check")Integer check);
    //购物车取消选中某个商品
    int unselectProduct(@Param("uid")Integer uid, @Param("productId")Integer productId, @Param("check")Integer check);
    //查询在购物车里的产品数量
    int getcartproductcount(Integer uid);
    //判断用户购物车是否全选
    int selectByUidCheck(@Param("uid")Integer uid, @Param("check")Integer check);
    //修改用户购物车是否全选
    int updateByUidCheck(@Param("uid")Integer uid, @Param("check")Integer check);
    //购物车更新有效信息
    int updatecartQuantity(Cart cartForQuantity);
  //查询选择的商品
    List<Cart> selectCheck(Integer uid);
//清空购物车
    int delectuid(@Param("list") List<Cart> list,@Param("uid") Integer uid);

}
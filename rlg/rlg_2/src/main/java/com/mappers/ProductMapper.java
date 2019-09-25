package com.mappers;

import com.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    //产品搜索及动态排序List
    List<Product> list(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword,
                       @Param("split1")String split1, @Param("split2") String split2);
    //产品detail
    Product detail(@Param("productId") Integer productId, @Param("is_new")Integer is_new,
                   @Param("is_hot")Integer is_hot, @Param("is_banner")Integer is_banner);

    //根据商品id进行查询
    Product selectByproductId(Integer productId);
    //查询商品的信息
    Product selectProductId(Integer productId);
    //根据id进行修改信息
    int updateId(@Param("productId")Integer productId, @Param("Stock")Integer Stock);
}
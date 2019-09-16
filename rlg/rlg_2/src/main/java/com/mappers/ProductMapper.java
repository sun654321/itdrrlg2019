package com.mappers;

import com.pojo.Categroy;
import com.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    //产品搜索及动态排序List
    List<Categroy> list(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword,
                        @Param("pageNum")Integer pageNum, @Param("pageSize") Integer pageSize);
    //产品detail
    List detail(Integer productId);
    //获取产品分类
    List topcategory(Integer sid);
    //根据商品id进行查询
    Product selectByproductId(Integer productId);
}
package com.mappers;

import com.common.ResponseCode;
import com.pojo.Categroy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategroyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Categroy record);

    int insertSelective(Categroy record);

    Categroy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Categroy record);

    int updateByPrimaryKey(Categroy record);
    //获取品类子节点
      List<Categroy> selectone(Integer categroyId);
    //增加节点
    int selectone1(@Param("parentId1") Integer parentId1, @Param("categroyName")String categroyName);
    //修改品类姓名
    int selectone2(@Param("categroyId")Integer categroyId,@Param("categroyName")String categroyName);
    //获取当前分类id及递归子节点categoryId
    List<Categroy> selectone3(Integer categroyId);
    //商品模块
    //获取产品分类
    List<Categroy> topcategory(Integer sid);
}
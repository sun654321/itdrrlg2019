package com.service.impl;

import com.common.ResponseCode;
import com.mappers.CategroyMapper;
import com.pojo.Categroy;
import com.service.CategroyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class CategroyServiceImpl implements CategroyService {
    @Autowired
    private CategroyMapper categroyMapper;
    //获取品类子节点
    @Override
    public ResponseCode selectone(Integer categroyId) {
        if(categroyId==null||categroyId.equals("")){
           return  ResponseCode.notseccessRs("输入为空");
        }
        //转为int
        List<Categroy> li = categroyMapper.selectone(categroyId);
        if (li == null) {
           return ResponseCode.notseccessRs(1,"未找到该品类");
        }
        return ResponseCode.seccessRs(li);
    }

    //增加节点
    @Override
    public ResponseCode selectone1(Integer parentId, String categroyName) {
        if(parentId==null||parentId.equals("")){
            return  ResponseCode.notseccessRs("ID为空");
        }
        if(categroyName==null||categroyName.equals("")){
            return  ResponseCode.notseccessRs("姓名为空");
        }
        int row = categroyMapper.selectone1(parentId,categroyName);
        if (row>0) {
           return ResponseCode.seccessRs(0,"增加成功");
        }
        return  ResponseCode.notseccessRs("添加失败");
    }

    //修改品类姓名
    @Override
    public ResponseCode selectone2(Integer categroyId, String categroyName) {
        if(categroyId==null||categroyId.equals("")){
            return  ResponseCode.notseccessRs("ID为空");
        }
        if(categroyName==null||categroyName.equals("")){
            return  ResponseCode.notseccessRs("姓名为空");
        }
        int row = categroyMapper.selectone2(categroyId,categroyName);
        if (row>0) {
            return ResponseCode.seccessRs(0,"修改成功");
        }else{
            return  ResponseCode.notseccessRs("修改失败");
        }
    }

    //获取当前分类id及递归子节点categoryId
    @Override
    public ResponseCode selectone3(Integer categroyId) {
        if(categroyId==null||categroyId.equals("")){
            return  ResponseCode.notseccessRs("输入为空");
        }
        List<Integer> li=new ArrayList<>();
        li.add(categroyId);
        getAll(categroyId,li);
        return  ResponseCode.seccessRs(0,li);
    }
    private  void getAll(Integer categroyId, List<Integer> list){
        List<Categroy> li=categroyMapper.selectone3(categroyId);
        if(li!=null&&li.size()!=0){
            for (Categroy Categroy:li) {
                list.add(Categroy.getId());
                getAll(Categroy.getId(),list);
            }

        }
    }

    //获取产品分类
    @Override
    public ResponseCode topcategory(Integer sid) {
        if (sid == null || sid<0) {
           return ResponseCode.notseccessRs("参数错误");
        }
        List list = categroyMapper.topcategory(sid);
        if (list == null) {
            return ResponseCode.notseccessRs("没有该ID");
        }
        if(list.size()==0){
            return ResponseCode.notseccessRs("没有子分类");
        }
        return ResponseCode.seccessRs(list);
    }
}



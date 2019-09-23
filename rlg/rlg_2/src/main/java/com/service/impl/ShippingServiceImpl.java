package com.service.impl;

import com.common.ResponseCode;
import com.mappers.ShippingMapper;
import com.pojo.Shipping;
import com.pojo.vo.ShippingVO;
import com.service.ShippingService;
import com.utils.PoToVoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    //创建收货地址
    @Override
    public ResponseCode  createAddress(Shipping shipping, Integer uid) {
        shipping.setUserId(uid);
        List<Shipping> ship = shippingMapper.selectAddress(uid);
        if (ship.size() != 0) {
            for (Shipping ship1 : ship) {
                if (ship1.getReceiverName().equals(shipping.getReceiverName())
                        && ship1.getReceiverPhone().equals(shipping.getReceiverPhone())
                        && ship1.getReceiverMobile().equals(shipping.getReceiverMobile())
                        && ship1.getReceiverProvince().equals(shipping.getReceiverProvince())
                        && ship1.getReceiverCity().equals(shipping.getReceiverCity())
                        && ship1.getReceiverDistrict().equals(shipping.getReceiverDistrict())
                        && ship1.getReceiverAddress().equals(shipping.getReceiverAddress())
                        && ship1.getReceiverZip().equals(shipping.getReceiverZip())
                        ) {
                    return ResponseCode.notseccessRs(1, "不可以重复添加");
                }
            }
        }
        int insert = shippingMapper.createAddress(shipping);
        if (insert <= 0) {
            return ResponseCode.notseccessRs(1, "添加失败");
        }
        List<Shipping> shippings = shippingMapper.selectAddress(uid);
        if (shippings == null||shippings.size()<=0) {
            return ResponseCode.notseccessRs(9, "没有收货地址");
        }
        List<ShippingVO> shippingVO = new ArrayList<>();
        for (Shipping shipping1 : shippings) {
            ShippingVO shippingVO1 = PoToVoUtil.ShipVO(shipping1);
            shippingVO.add(shippingVO1);
        }
        return ResponseCode.seccessRs(shippingVO);
    }

    //修改收货地址的信息
    @Override
    public ResponseCode updateAddress(Shipping shipping, Integer uid) {
        shipping.setUserId(uid);
        List<Shipping> ship = shippingMapper.selectAddress(uid);
        if (ship.size() != 0) {
            for (Shipping ship1 : ship) {
                if (ship1.getReceiverName().equals(shipping.getReceiverName())
                        && ship1.getReceiverPhone().equals(shipping.getReceiverPhone())
                        && ship1.getReceiverMobile().equals(shipping.getReceiverMobile())
                        && ship1.getReceiverProvince().equals(shipping.getReceiverProvince())
                        && ship1.getReceiverCity().equals(shipping.getReceiverCity())
                        && ship1.getReceiverDistrict().equals(shipping.getReceiverDistrict())
                        && ship1.getReceiverAddress().equals(shipping.getReceiverAddress())
                        && ship1.getReceiverZip().equals(shipping.getReceiverZip())
                        ) {
                    return ResponseCode.notseccessRs(1, "修改的信息没有发生变化");
                }
            }
        }
        int i = shippingMapper.updateAddress(shipping);
        if (i <= 0) {
            return ResponseCode.notseccessRs(1, "修改失败");
        }

        List<Shipping> shippings = shippingMapper.selectAddress(uid);
        if (shippings == null||shippings.size()<=0) {
            return ResponseCode.notseccessRs(9, "没有收货地址");
        }
        List<ShippingVO> shippingVO = new ArrayList<>();
        for (Shipping shipping1 : shippings) {
            ShippingVO shippingVO1 = PoToVoUtil.ShipVO(shipping1);
            shippingVO.add(shippingVO1);
        }
        return ResponseCode.seccessRs(shippingVO);
    }

    //删除收货地址
    @Override
    public ResponseCode deleteAddress(Integer sid, Integer uid) {
        int i = shippingMapper.deleteAddress(sid, uid);
        if (i <= 0) {
            return ResponseCode.notseccessRs(1, "删除失败");
        }
        List<Shipping> shippings = shippingMapper.selectAddress(uid);
        if (shippings == null||shippings.size()<=0) {
            return ResponseCode.notseccessRs(9, "没有收货地址");
        }
        List<ShippingVO> shippingVO = new ArrayList<>();
        for (Shipping shipping1 : shippings) {
            ShippingVO shippingVO1 = PoToVoUtil.ShipVO(shipping1);
            shippingVO.add(shippingVO1);
        }
        return ResponseCode.seccessRs(shippingVO);
    }

    //查询某一个详细的收货地址信息
    @Override
    public ResponseCode selectOne(Integer sid, Integer uid) {
        if (sid == null || sid <= 0) {
            return ResponseCode.notseccessRs("id不能为空");
        }
        Shipping shipping = shippingMapper.selectOne(sid, uid);
        if (shipping == null) {
            return ResponseCode.notseccessRs("id输入有误");
        }
        return ResponseCode.seccessRs(shipping);
    }
}

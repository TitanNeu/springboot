package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.PromoDOMapper;
import com.miaoshaproject.dataobject.PromoDO;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @program: SecondKill
 * @description: 促销服务实现
 * @author: Mr.Niu
 * @create: 2019-06-23 11:48
 **/

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDOMapper promoDOMapper;


    @Override
    public PromoModel getPromoModelByItemId(Integer itemId) {

        //根据itemId获取数据模型PromoDO
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        //data object -> data model
        PromoModel promoModel = convertFromPromoDO(promoDO);

        if(promoModel == null){
            return null;
        }

        //判断当前秒杀活动的状态
        if(promoModel.getStartDate().isAfterNow()){
            //未开始
            promoModel.setStatus(1);
        } else if(promoModel.getEndDate().isBeforeNow()){
            //已结束
            promoModel.setStatus(3);
        } else{
            //正进行
            promoModel.setStatus(2);
        }



        return promoModel;
    }

    private PromoModel convertFromPromoDO(PromoDO promoDO){
        if(promoDO == null)
            return null;
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDO.getEndDate()));

        return promoModel;

    }
}


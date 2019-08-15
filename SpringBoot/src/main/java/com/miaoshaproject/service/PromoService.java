package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * @program: SecondKill
 * @description: 促销服务接口
 * @author: Mr.Niu
 * @create: 2019-06-23 11:40
 **/

public interface PromoService {

    //根据itemId获得正在进行或者即将进行的秒杀活动
    PromoModel getPromoModelByItemId(Integer itemId);
}


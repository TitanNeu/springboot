package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.OrderModel;

/**
*
 * @Author ChinaDick
 * @Description 交易功能的服务层
 * @Date 21:18 2019/6/15
 * @Param
 * @return
 **/
public interface OrderService {
    //userId, itemId, 以及item的数量amount，就可以确定一个订单
    /**推荐：1.通过前端url传来秒杀id，下单接口内校验对应id是否是对应商品，以及活动是否开始
     *
     *不推荐：2。直接在下单接口内判断对应的商品是否存在秒杀，若存在进行中的秒杀，则以秒杀价格下单
     *
     * */

    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
}

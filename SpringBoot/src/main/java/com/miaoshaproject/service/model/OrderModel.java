package com.miaoshaproject.service.model;

import java.math.BigDecimal;

/**
 * @program: SecondKill
 * @description:交易模型,用于服务层的操作的对象，所有数据来源于DO
 * @author: Mr.Niu
 * @create: 2019-06-15 20:52
 **/

public class OrderModel {

    //秒杀数据模型的id，若非空，则表示以秒杀的方式下单
    private Integer promoId;

    //订单模型的id
    private String id;

    //用户id
    private Integer userId;

    //购买商品的单价；若promoId非空,则是秒杀单价
    private BigDecimal itemPrice;

    //商品模型id
    private Integer itemId;

    //购买商品数量
    private Integer amount;

    //购买总金额；若promoId非空,则是秒杀总价格
    private BigDecimal orderPrice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}


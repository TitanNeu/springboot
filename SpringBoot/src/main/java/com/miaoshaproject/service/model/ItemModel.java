package com.miaoshaproject.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: SecondKill
 * @description: 商品模型
 * @author: Mr.Niu
 * @create: 2019-05-19 10:41
 **/

public class ItemModel {

    //聚合模型，聚合ProModel,若PromoModel不为空，则有秒杀活动
    private PromoModel promoModel;

    private Integer id;
    //商品名称
    @NotBlank(message = "商品名不能为空")
    private String title;

    //商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 1, message = "商品价格大于0")
    private BigDecimal price;

    //库存
    @NotNull(message = "库存不能空")
    private Integer stock;

    //描述
    @NotBlank(message = "商品描述不能为空")
    private String description;

    //销量（不是入参，不需要入参检测）
    private Integer sales;

    //描述图的url
    @NotBlank(message = "图片不能为空")
    private String imgUrl;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PromoModel getPromoModel() {
        return promoModel;
    }

    public void setPromoModel(PromoModel promoModel) {
        this.promoModel = promoModel;
    }
}


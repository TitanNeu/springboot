package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: SecondKill
 * @description: 创建商品的controller
 * @author: Mr.Niu
 * @create: 2019-05-26 16:15
 **/
@Controller("/item")
@RequestMapping("/item")
@CrossOrigin(origins = "*", allowCredentials = "true")


public class ItemController extends BaseController{

    @Autowired
    ItemService itemService;

    //创建商品的controller
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})

    @ResponseBody
    public CommonReturnType creatItem(@RequestParam(name = "title")String title,
                                      @RequestParam(name = "price") BigDecimal price,
                                      @RequestParam(name = "description")String description,
                                      @RequestParam(name = "stock")Integer stock,
                                      @RequestParam(name = "imgUrl")String imgUrl
                                      ) throws BusinessException {

        //封装Service请求创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);



//        ItemModel itemModelForReturn = itemService.createItem(itemModel);
//        ItemVO itemVO = convertVOFromModel(itemModelForReturn);
//        return CommonReturnType.create(itemVO);

        //简化版，这里和视频不一样，只要实现数据插入数据库就可以了
        itemService.createItem(itemModel);
        return CommonReturnType.create(itemModel);

    }

    //商品详情页浏览
    @RequestMapping(value = "/get", method = {RequestMethod.GET})
    @ResponseBody

    public CommonReturnType getItem(@RequestParam(name="id") Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemModel);

        return CommonReturnType.create(itemVO);
    }

    //商品列表浏览
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        //利用stream API，将itemModelList的每一个ItemModel对象，转换为ItemVO对象并映射到itemVOList
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            return convertVOFromModel(itemModel);
        }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }

    //从商品业务层模型到视图模型转换
    private ItemVO convertVOFromModel(ItemModel itemModel){

        if(itemModel == null)
            return null;

        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);

        //从商品模型，获得它聚合的秒杀模型
        PromoModel promoModel = itemModel.getPromoModel();
        if(promoModel != null){
            //有正在进行或即将进行的秒杀活动
            itemVO.setPromoStatus(promoModel.getStatus());
            itemVO.setPromoId(promoModel.getId());
            itemVO.setPromoPrice(promoModel.getPromoItemPrice());
            itemVO.setStartDate(promoModel.getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setEndDate(promoModel.getEndDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));

        } else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }

}


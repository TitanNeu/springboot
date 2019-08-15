package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnumBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.PromoModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: SecondKill
 * @description:
 * @author: Mr.Niu
 * @create: 2019-05-26 14:45
 **/

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private PromoService promoService;

    //将itemModel核心模型转换成ItemDO数据对象
    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if(itemModel == null)
            return null;
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        //itemDO里面price是double，itemModel里面是Big Decimal,需要转换
        itemDO.setPrice(itemModel.getPrice().doubleValue());

        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel){
        if(itemModel == null)
            return null;
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());

        return itemStockDO;
    }


    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        //转换ItemModel为DataObject
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);


        //写入数据库
        //这里无法继续执行，原因是数据库中url字段长度设置成了0，导致sql无法执行
        this.itemDOMapper.insertSelective(itemDO);

        itemModel.setId(itemDO.getId());

        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);

        this.itemStockDOMapper.insertSelective(itemStockDO);


        //返回创建完成的对象。
        // 这一句有异常，虽然前面可以插入数据库，但浏览器总返回错误信息。还未解决,暂时return null可以通过，显示创建成功
       // return getItemById(itemDO.getId());
        return null;
    }

    //查询列表操作方法
    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();

        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            //根据itemDO的id获取itemStockDO
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            return convertModelFromDataObject(itemDO, itemStockDO);

        }).collect(Collectors.toList());
        return itemModelList;
    }


    //通过item 的id获取核心领域模型
    @Override
    public ItemModel getItemById(Integer id) {
        //通过id获取itemDO
        ItemDO itemDO = this.itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null)
            return null;

        //通过itemDO的id获得库存记录的DO
        ItemStockDO itemStockDO = this.itemStockDOMapper.selectByItemId(itemDO.getId());

        //data object -> model:将ItemDO和ItemStockDO两个数据对象转换为ItemModel，用于业务逻辑的操作
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);

        //获取秒杀活动商品模型
        PromoModel promoModel = promoService.getPromoModelByItemId(itemModel.getId());
        //秒杀模型存在，并且秒杀未结束，将秒杀模型聚合到商品模型
        if(promoModel != null && promoModel.getStatus() != 3){
            itemModel.setPromoModel(promoModel);
        }

        return itemModel;

    }
    

    /*
    *
     * @Author ChinaDick
     * @Description：库存扣减
     * @Date 21:46 2019/6/15
     * @Param [itemId, amount]
     * @return boolean
     **/
    @Override
    @Transactional //保持事务一致性。减操作的时候，更新操作会对item_stock的数据库表的行加锁
    public boolean decreaseStock(Integer itemId, Integer amount) throws BusinessException {
        //affectedRows: 成功更新的条目数
        int affectedRows = itemStockDOMapper.decreaseStock(itemId, amount);
        if(affectedRows > 0){
            //更新成功
            return true;
        } else {
            //更新失败
            return false;
        }

    }

    /**减库存的操作*/
    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) throws BusinessException {
        itemDOMapper.increaseSales(itemId, amount);
    }

    //将ItemDO和ItemStockDO对象转换为业务逻辑操作的核心领域模型
    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        //注意DO和Model里面同字段类型的转换
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());

        return itemModel;
    }
}


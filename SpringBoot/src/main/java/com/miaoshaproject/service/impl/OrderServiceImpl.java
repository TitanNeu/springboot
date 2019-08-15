package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnumBusinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: SecondKill
 * @description: OrderService的实现类
 * @author: Mr.Niu
 * @create: 2019-06-15 21:21
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    OrderDOMapper orderDOMapper;

    @Autowired
    SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    //保证以下的操作都在一个事务中
    public OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        /**1.校验下单状态：*/
        //商品是否存在，
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }
        //用户是否合法，
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }
        //购买数量是否合理
        if(amount <= 0 || amount > 99){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "购买数量不正确");
        }

        //校验活动信息
        if(itemModel.getPromoModel() != null){
            if(promoId != null){
                //(1)校验对应活动是否适用于这个商品
                if(promoId.intValue() != itemModel.getPromoModel().getId()){
                    throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息错误");
                }else if(itemModel.getPromoModel().getStatus().intValue() != 2){
                    //(2)校验活动是否在进行中
                    throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR,"活动未开始或已结束");
                }


            }
        }

        /**2.落单减库存*/
        boolean result = itemService.decreaseStock(itemId, amount);
        if(!result){
            //库存不足
            throw new BusinessException(EnumBusinessError.STOCK_NOT_ENOUGH);
        }

        /**3.订单入库*/
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        orderModel.setItemId(itemId);
        //获取itemId对应的商品itemModel的单价，设置orderModel的单价字段
        if(promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            orderModel.setItemPrice(itemModel.getPrice());
        }
        //把promoId入库
        orderModel.setPromoId(promoId);
        //设置orderModel的订单总价 = 商品单价 * 订单数量
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        //生成交易流水号，作为orderDO对应数据库表记录的主键id
        orderModel.setId(generateOrderNum());


        //Model -> Data Object
        OrderDO orderDO = convertOrderModel(orderModel);

        //订单数据对象orderDO，通过对应DAO层对象orderDOMapper写入数据库
        orderDOMapper.insertSelective(orderDO);
        //商品销量字段增加
        itemService.increaseSales(itemId, amount);

        /**4.返回前端*/
        return orderModel;
    }

    //REQUIRES_NEW代表，若以下事务被另一个事务使用，不管外部事务成功与否，下面的子事务只要成功就会提交
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //订单号的生成
    String generateOrderNum(){
        /**订单号16位*/
        StringBuilder stringBuilder = new StringBuilder();

        /**前8位时间信息：年月日*/
        LocalDateTime now = LocalDateTime.now();
        //curDate是20190616这种格式
        String curDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(curDate);

        /**中间6位是自增序列*/
        //获取当前的sequenceDO,这张sequence_info的数据库表是为了产生自增序列而建立的

        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();

        //重新设置表记录current_value字段值
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        //将sequenceDO，根据主键更新到数据库
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        //若sequenceStr不足6位，则填充0,然后再添加字符串
        for(int i = 0; i < 6-sequenceStr.length(); i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        /**最后两位是分库分表位，通过%100可以获得最后两位数字.暂时写死为00*/
        stringBuilder.append("00");

        return stringBuilder.toString();
    }


    //OrderModel到OrderDO的转换
    private OrderDO convertOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        //Model里面单价和总价的类型是BigDecimal，DO里面是Double类型，所以要转换一下
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }

}


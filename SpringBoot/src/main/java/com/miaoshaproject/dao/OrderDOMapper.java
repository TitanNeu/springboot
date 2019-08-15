package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.OrderDO;
import org.springframework.stereotype.Component;

/*
*
 * @Author ChinaDick
 * @Description //操作订单表记录的DAO层对象
 * @Date 21:10 2019/6/15
 * @Param
 * @return
 **/
@Component
public interface OrderDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    int insert(OrderDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    int insertSelective(OrderDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    OrderDO selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    int updateByPrimaryKeySelective(OrderDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_info
     *
     * @mbg.generated Sat Jun 15 21:08:13 CST 2019
     */
    int updateByPrimaryKey(OrderDO record);


}
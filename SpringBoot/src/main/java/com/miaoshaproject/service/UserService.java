package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

public interface UserService {
    //通过用户id获取用户领域模型对象的方法
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    //telephone是用户的手机，encryptPassword是加密的密码
    UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException;
}

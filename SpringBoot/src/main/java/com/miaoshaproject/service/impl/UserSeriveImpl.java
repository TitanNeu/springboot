package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDOMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDO;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnumBusinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: SecondKill
 * @description: UserService接口的实现类
 * @author: Mr.Niu
 * @create: 2019-05-10 09:21
 **/

@Service
public class UserSeriveImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    //创建核心领域模型
    @Override
    public UserModel getUserById(Integer id) {

        //调用userDOMapping根据id获取用户DataObject
        //这个UserDO不能透传给前端
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if(userDO == null){
            return null;
        }
        //通过userDO的id，获取用户的加密的密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDO(userDO, userPasswordDO);


    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        /*if(StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
        || StringUtils.isEmpty(userModel.getTelephone()))
        {
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        //实现model->dataobject
        UserDO userDO = convertFromModel(userModel);


        try {
            //持久化DO, insertSelective可以自动判空的dataobject字段，null的话字段数据就不会插入数据库
            //可能会产生重复注册的异常
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex){
            throw new BusinessException(EnumBusinessError.PARAMETER_VALIDATION_ERROR, "手机号重复注册");
        }

        //插入数据之后，将主键取出给userModel，注意设置UserDOMapping才能取出主键
        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);

        return;
    }

    @Override
    public UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
        if(userDO == null){
            throw new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDO(userDO, userPasswordDO);

        //比对用户信息加密的密码是否和传输进来的密码匹配，注意修改UserDOMapper使得可以通过telephone查找
        if(!StringUtils.equals(encryptPassword, userModel.getEncryptPassword())){
            throw new BusinessException(EnumBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;

    }

    //usermodel 转 userdataobject
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    //userModel转密码的数据对象模型
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null)
            return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());

        return userPasswordDO;
    }

    /*
    *
     * @Author ChinaDick
     * @Description //将DO模型转换为业务处理的核心领域用户模型，但这个模型也不能直接透传给前端
     * @Date 14:26 2019/5/12
     * @Param [userDO, userPasswordDO]
     * @return com.miaoshaproject.service.model.UserModel
     **/
    private UserModel convertFromDO(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null)
            return null;
        //创建领域模型
        UserModel userModel = new UserModel();
        //将userDO的属性拷贝到userModel，必须保证前后同名属性的类型一致
        BeanUtils.copyProperties(userDO, userModel);

        if(userPasswordDO != null){
            //直接向userModel注入字段值
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }

        return userModel;
    }
}


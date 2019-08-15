package com.miaoshaproject.error;

/**
 * @program: SecondKill
 * @description: 异常类
 * @author: Mr.Niu
 * @create: 2019-05-12 15:25
 **/
//包装器业务异常类实现
public class BusinessException extends Exception implements CommonError {

    //聚合一个CommonError
    private CommonError commonError;

    //接收EnumBusinessesError的传参，创建业务异常
    public BusinessException(CommonError commonError) {
        //首先使用父类的初始化
        super();
        this.commonError = commonError;
    }


    //接收自定义errMsg的方式构造业务异常
    public BusinessException(CommonError commonError, String errMsg){

        //首先使用父类Exception的初始化
        super();

        this.commonError = commonError;
        this.commonError.setErrorMsg(errMsg);
    }

    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.commonError.setErrorMsg(errorMsg);
        return this;
    }
}


package com.miaoshaproject.error;

/**
*
 * @Author ChinaDick
 * @Description //错误信息枚举,统一管理错误码
 * @Date 15:19 2019/5/12
 * @Param
 * @return
 **/
public enum EnumBusinessError implements CommonError {

    //枚举类内部的枚举类型，实际是一个static的EnumBusinessError对象

    /**通用错误类型10001，参数类型错误*/
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),
    /**未知类型错误*/
    UNKNOW_ERROR(10002, "未知错误"),
    /**20000是用户相关的错误定义*/
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户名或密码错误"),
    USER_NOT_LOGIN(20003, "用户还未登录"),

    //30000是交易信息错误
    STOCK_NOT_ENOUGH(300001, "库存不足")
    ;


    //枚举类型的属性
    private int errCode;
    private String errMsg;

    //枚举类型的构造方法，是私有的。外部不能创建枚举对象
    private EnumBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errMsg = errorMsg;
        return this;
    }
}

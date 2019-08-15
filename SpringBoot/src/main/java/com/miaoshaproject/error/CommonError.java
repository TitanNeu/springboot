package com.miaoshaproject.error;

/**
 * @program: SecondKill
 * @description: 统一错误返回格式的接口
 * @author: Mr.Niu
 * @create: 2019-05-12 15:11
 **/

public interface CommonError {

    int getErrorCode();

    String getErrorMsg();

    CommonError setErrorMsg(String errorMsg);

}


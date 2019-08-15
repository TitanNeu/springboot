package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EnumBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SecondKill
 * @description:
 * @author: Mr.Niu
 * @create: 2019-05-12 19:25
 **/

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";


    //定义Exception Handler捕获未被Controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody

    public Object handlerException(HttpServletRequest request, Exception ex){

        Map<String, Object> hashMap = new HashMap<>();

        if(ex instanceof BusinessException){ //
            BusinessException businessException = (BusinessException)ex;
            hashMap.put("errCode", businessException.getErrorCode());
            hashMap.put("errMsg", businessException.getErrorMsg());
        } else {
            hashMap.put("errCode", EnumBusinessError.UNKNOW_ERROR.getErrorCode());
            hashMap.put("errMsg", EnumBusinessError.UNKNOW_ERROR.getErrorMsg());

        }

        return CommonReturnType.create(hashMap, "fail");
    }
}


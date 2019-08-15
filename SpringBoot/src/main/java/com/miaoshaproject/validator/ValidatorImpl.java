package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @program: SecondKill
 * @description:暂时这里先不写
 * @author: Mr.Niu
 * @create: 2019-05-15 10:18
 **/
@Component
public class ValidatorImpl implements InitializingBean {

    //javax的
    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean){
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintValidationSet = validator.validate(bean);
        if(constraintValidationSet.size() > 0){
            result.setHasErrors(true);
            constraintValidationSet.forEach(constraintValidation->{
                String errMsg = constraintValidation.getMessage();
                String properName = constraintValidation.getPropertyPath().toString();
                result.getErrMsgMap().put(properName, errMsg);

            });
        }

        return result;
    }

    @Override
    //bean初始化完成之后会回调这个方法
    public void afterPropertiesSet(){
        //将hibernate的 validdator通过工厂的初始化方式实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

}


package com.miaoshaproject.response;

/**
 * @program: SecondKill
 * @description: 返回给前端的通用数据格式，status:data格式
 * @author: Mr.Niu
 * @create: 2019-05-12 14:49
 **/

public class CommonReturnType {

    //对应请求的返回处理结果，“success”或”fail“
    private String status;

    /**若status=success，则返回前端需要的json数据
     */

    private Object data;

    //定义通用的创建方法，返回信息
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result, "success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }





    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}


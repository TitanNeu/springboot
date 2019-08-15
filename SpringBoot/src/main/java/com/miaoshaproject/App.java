package com.miaoshaproject;

import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dao.UserDOMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
//包中的类被Spring容器托管
@SpringBootApplication(scanBasePackages = {"com.miaoshaproject"})
@RestController
//将DAO类存放的地方设置在这个注解下，数据访问对象DAO执行对数据对象DO的增删改查等操作
@MapperScan("com.miaoshaproject.dao")
public class App
{
    @Autowired
    private UserDOMapper userDOMapper;

    @RequestMapping("/")
    public String home(){
        UserDO userDO = userDOMapper.selectByPrimaryKey(0);
        if(userDO == null){
            return "用户对象不存在";
        }
        return userDO.getName();
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);
    }
}

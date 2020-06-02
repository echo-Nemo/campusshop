package com.echo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"com.echo"})
@MapperScan("com.echo.dao")
//开启缓存
@EnableCaching
@ServletComponentScan
public class CampusshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusshopApplication.class, args);
    }

}

package com.example.sharding_jdbc_demo3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath*:sharding-jdbc.xml")
@MapperScan("com.example.sharding_jdbc_demo3.dao")
public class ShardingJdbcDemo3Application {

    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcDemo3Application.class, args);
    }

}

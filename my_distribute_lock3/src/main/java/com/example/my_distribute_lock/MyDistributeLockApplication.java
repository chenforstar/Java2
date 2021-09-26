package com.example.my_distribute_lock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.my_distribute_lock.dao")
@EnableScheduling//定时
public class MyDistributeLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyDistributeLockApplication.class, args);
    }

}

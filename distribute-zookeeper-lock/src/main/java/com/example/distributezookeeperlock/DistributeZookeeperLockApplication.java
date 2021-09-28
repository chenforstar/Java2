package com.example.distributezookeeperlock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DistributeZookeeperLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeZookeeperLockApplication.class, args);
    }


    //连接zookeeper的方法
    //指定初始化方法start(),close()
    @Bean(initMethod = "start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        return client;
    }

}

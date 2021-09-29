package com.example.redisson_lock.controller;

/*
  @Classname RedissonLockController
  @Description TODO
  @Date 2021/9/28 21:10
  @Created by lc
 */

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class RedissonLockController {
    @Autowired
    private RedissonClient redissonClient;
    @RequestMapping("redissonLock")
    public String redissonLock(){
        //使用Java api调用这个分布式锁
        log.info("我进入了方法");
        // 1. Create config object
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        // 2. Create Redisson instance
//        // Sync and Async API
//        RedissonClient redisson = Redisson.create(config);
//
//
        RLock rlock = redissonClient.getLock("/order");
        try {
            rlock.lock(30, TimeUnit.SECONDS);//超时时间，过期锁自动释放
            log.info("我拿到了锁！");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            rlock.unlock();
            log.info("我释放了锁！");
        }

        return "方法执行完毕！";

    }
}

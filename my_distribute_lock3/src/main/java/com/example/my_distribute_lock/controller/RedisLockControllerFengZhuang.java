package com.example.my_distribute_lock.controller;

/*
  @Classname RedisLockController
  @Description 分布式事务所，使用唯一key作为锁
  @Date 2021/9/26 10:55
  @Created by lc
 */

import com.example.my_distribute_lock.lock.redisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class RedisLockControllerFengZhuang {
    @Autowired
    private RedisTemplate redisTemplate;
    private String key = "redisKey";
    private int expireTime = 30;
    @RequestMapping("redisLockfz")
    public String redisLockfz(){
        log.info("我进入了方法");

//        boolean lock = redislock.getLock();
//        if(lock==true){
//            log.info("获得了锁");
//            try {
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }finally {
//                //手动关闭
//                boolean result = redislock.unlock();
//                log.info("释放锁的结果： "+result);
//            }
//
//        }
        //获得锁的过程放在redis里，那么执行完毕，自动释放锁
        try(redisLock redislock = new redisLock(redisTemplate,key,expireTime)){
            if(redislock.getLock()==true){
                log.info("获得了锁");
                Thread.sleep(10000);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        log.info("方法执行完成");
        return "方法执行完成";
    }
}

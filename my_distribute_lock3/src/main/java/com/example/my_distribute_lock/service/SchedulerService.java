package com.example.my_distribute_lock.service;

import com.example.my_distribute_lock.lock.redisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
  @Classname SchedulerService
  @Description 定时任务
  @Date 2021/9/26 21:42
  @Created by lc
 */
@Service
@Slf4j
public class SchedulerService {
    @Autowired
    private RedisTemplate redisTemplate;
    private String key = "redisKey";
    private int expireTime = 30;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendSms(){
        //如果两个服务器执行这个定时任务，那么可能同一时刻会同时执行，就会重复执行
        //使用分布式锁避免这个问题
        try(redisLock redislock = new redisLock(redisTemplate,key,expireTime)){
            if(redislock.getLock()==true){
                //log.info("获得了锁");
                log.info("每五秒钟向18888888888发送短信");
                //Thread.sleep(10000);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}

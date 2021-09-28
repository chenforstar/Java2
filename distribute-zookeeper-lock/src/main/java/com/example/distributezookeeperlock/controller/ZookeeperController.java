package com.example.distributezookeeperlock.controller;

import com.example.distributezookeeperlock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/*
  @Classname ZookeeperController
  @Description TODO
  @Date 2021/9/28 16:28
  @Created by lc
 */
@RestController
@Slf4j
public class ZookeeperController {
    @RequestMapping("zkLock")
    public String zookeeperLock(){
        log.info("我进入了方法！");
        try(ZkLock zkLock = new ZkLock()) {
            if(zkLock.getLock("order")){
                log.info("我获得了锁！");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("方法执行完成！");
        return "方法执行完成！";
    }

    @Autowired
    private CuratorFramework client;

    @RequestMapping("curatorLock")
    public String curatorLock(){
        log.info("我进入了方法！");
        //client.start();
        String lockPath = "/order";
        long maxWait = 30;
        TimeUnit waitUnit = TimeUnit.SECONDS;
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        try {
            if ( lock.acquire(maxWait, waitUnit) )
            {
                try
                {
                    // do some work inside of the critical section here
                    log.info("我获得了锁");
                    Thread.sleep(10000);
                }
                finally
                {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //client.close();
        log.info("方法执行完成！");
        return "方法执行完成！";
    }
}

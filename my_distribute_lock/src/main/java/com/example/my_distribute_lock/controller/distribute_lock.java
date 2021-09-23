package com.example.my_distribute_lock.controller;

/*
  @Classname distribute_lock
  @Description TODO
  @Date 2021/9/23 10:30
  @Created by lc
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Slf4j
public class distribute_lock {
    //用来模拟两个请求，都可以进入方法，但是只有一个能拿到锁
    @RequestMapping("singleLock")
    public String singleLock(){
        log.info("我进入了方法");
        lock.lock();
        log.info("我获得了锁");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
        return "我已经执行完了任务";

    }
    private Lock lock = new ReentrantLock();
}

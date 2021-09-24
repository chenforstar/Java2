package com.example.my_distribute_lock.controller;

/*
  @Classname distribute_lock
  @Description TODO
  @Date 2021/9/23 10:30
  @Created by lc
 */

import com.example.my_distribute_lock.dao.DistributeLockMapper;
import com.example.my_distribute_lock.model.DistributeLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Slf4j
public class distribute_lock {

    @Resource
    private DistributeLockMapper distributeLockMapper;//分布式锁

    @RequestMapping("singleLock")
    @Transactional(rollbackFor = Exception.class)
    public String singleLock() throws Exception {
        log.info("我进入了方法");
        DistributeLock distributeLock = distributeLockMapper.selectDistributeLock("demo");
        if(distributeLock==null){
            throw new Exception("分布式锁找不到");
        }
        log.info("我获得了锁");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "我已经执行完了任务";

    }
    private Lock lock = new ReentrantLock();
}

package com.example.redisson_lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class RedissonLockApplicationTests {

    @Test
    void contextLoads() {
    }
    
    @Test
    public void testRedisson(){
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        // 2. Create Redisson instance
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        RLock rlock = redisson.getLock("/order");


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



    }




}

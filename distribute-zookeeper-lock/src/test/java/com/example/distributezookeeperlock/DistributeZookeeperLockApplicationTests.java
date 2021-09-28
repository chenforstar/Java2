package com.example.distributezookeeperlock;

import com.example.distributezookeeperlock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class DistributeZookeeperLockApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testZkLock() throws Exception{
        ZkLock zkLock = new ZkLock();
        boolean lock = zkLock.getLock("order");
        log.info("获取锁的信息 = "+lock);
        zkLock.close();
    }

}

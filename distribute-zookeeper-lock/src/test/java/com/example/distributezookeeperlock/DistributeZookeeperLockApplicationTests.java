package com.example.distributezookeeperlock;

import com.example.distributezookeeperlock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

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

    @Test
    public void testCuratorLock() throws Exception{
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();

        String lockPath = "/order";
        long maxWait = 30;
        TimeUnit waitUnit = TimeUnit.SECONDS;
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        if ( lock.acquire(maxWait, waitUnit) )
        {
            try
            {
                // do some work inside of the critical section here
                log.info("我获得了锁");
            }
            finally
            {
                lock.release();
            }
        }
    }


}

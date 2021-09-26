package com.example.my_distribute_lock.controller;

/*
  @Classname RedisLockController
  @Description 分布式事务所，使用唯一key作为锁
  @Date 2021/9/26 10:55
  @Created by lc
 */

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
public class RedisLockController {
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("redisLock")
    public String redisLock(){
        log.info("我进入了方法");
        String key = "redisKey";
        String value = UUID.randomUUID().toString();//每个线程保证唯一
        //RedisCallback让RedisTemplate进行回调，通过它们可以在同一条连接下执行多个Redis命令
        //RedisStringCommands.SetOption->SET command arguments for NX, XX.
        //// 被回调函数，声明一个锁，作用是set一个值是否成功
        RedisCallback redisCallback = connection -> {

            //// nx(一个锁)，如果为false表示设置失败，为true表示该key未被设置过
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();

            Expiration expiration = Expiration.seconds(30);//过期时间设置为30s

            byte[] rediskey = redisTemplate.getKeySerializer().serialize(key);//对key进行序列化
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);//对key进行序列化


            Boolean result = connection.set(rediskey, redisValue, expiration, setOption);
            return result;

        };

        //获取分布式锁
        Boolean lock = (Boolean)redisTemplate.execute(redisCallback);//获得锁
        if(lock==true){
            log.info("我进入了锁");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                //使用lua脚本，释放锁
                String script = "if redis.call(\"get\",KEYS[1])==ARGV[1] then\n" +
                        "   return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "   return 0\n" +
                        "end";
                RedisScript<Boolean> redisScript = RedisScript.of(script,Boolean.class);
                List<String> keys = Arrays.asList(key);
                Boolean result = (Boolean)redisTemplate.execute(redisScript, keys, value);
                log.info("释放锁的结果： "+result);
            }
        }

        log.info("方法执行完成");
        return "方法执行完成";
    }
}

package com.example.my_distribute_lock.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
  @Classname redisLock
  @Description 封装锁
  @Date 2021/9/26 21:08
  @Created by lc
 */

@Slf4j
public class redisLock implements AutoCloseable{
    private RedisTemplate redisTemplate;
    private String key;
    private String value;//不能往外面暴露
    //单位：秒
    private int expireTime;//过期时间

    //构造函数
    public redisLock(RedisTemplate redisTemplate,String key,int expireTime){
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime = expireTime;
        //value是生成的
        this.value = UUID.randomUUID().toString();
    }
    //获取锁的方法
    public boolean getLock(){
        RedisCallback redisCallback = connection -> {

            //// nx(一个锁)，如果为false表示设置失败，为true表示该key未被设置过
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();

            Expiration expiration = Expiration.seconds(expireTime);//过期时间设置为30s

            byte[] rediskey = redisTemplate.getKeySerializer().serialize(key);//对key进行序列化
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);//对key进行序列化


            Boolean result = connection.set(rediskey, redisValue, expiration, setOption);
            return result;

        };
        Boolean lock = (Boolean)redisTemplate.execute(redisCallback);//获得锁
        return lock;
    }

    public boolean unlock(){
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
        return result;
    }


    /**
     * 实现自动关闭
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        unlock();//调用的时候就可以不用写finally了
    }
}

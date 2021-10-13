package com.example.sharding_jdbc_demo3.sharding;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

/*
  @Classname MySharding
  @Description 自定义分片方法
  @Date 2021/10/13 17:15
  @Created by lc
 */
public class MySharding implements PreciseShardingAlgorithm<String> {
    /**
     * collection 指的是表对应的编号
     * preciseShardingValue 对应的值
     * @param collection
     * @param preciseShardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String id = preciseShardingValue.getValue();
        System.out.println("id = "+id);
        System.out.println("collection.size() = "+collection.size());
        int mode = Math.abs(id.hashCode()%collection.size());
        String[] strings = collection.toArray(new String[0]);
        System.out.println(strings[0]+"-----"+strings[1]);
        System.out.println("mode = "+mode);
        return strings[mode];
    }
}

package com.example.sharding_jdbc_demo3;

import com.example.sharding_jdbc_demo3.dao.OrderMapper;
import com.example.sharding_jdbc_demo3.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
class ShardingJdbcDemo3ApplicationTests {

    @Resource
    private OrderMapper orderMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testOrder(){
        Order order = new Order();
        order.setUserId(12);//用户id->选择哪个数据库
        order.setOrderId(1);//选择哪个表

        orderMapper.insert(order);


    }

}

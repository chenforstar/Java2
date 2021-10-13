package com.example.sharding_jdbc_demo;

import com.example.sharding_jdbc_demo.dao.AreaMapper;
import com.example.sharding_jdbc_demo.dao.OrderMapper;
import com.example.sharding_jdbc_demo.model.Area;
import com.example.sharding_jdbc_demo.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

@SpringBootTest
class ShardingJdbcDemoApplicationTests {

    @Resource
    private OrderMapper orderMapper;
    private AreaMapper  areaMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testOrder(){
        Order order = new Order();
        order.setUserId(13);//用户id->选择哪个数据库
        order.setId(2);//选择哪个表
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insert(order);


    }

    @Test
    void testArea(){
        Area area = new Area();
        area.setId(1);
        area.setName("jason");
//        areaMapper.insert(area);
        Area area1 = areaMapper.selectByPrimaryKey(1);
//        System.out.println(a);
    }

}

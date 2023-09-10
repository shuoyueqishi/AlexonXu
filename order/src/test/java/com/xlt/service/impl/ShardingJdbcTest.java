package com.xlt.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import com.xlt.mapper.IOrderMapper;
import com.xlt.model.po.OrderPo;
import com.xlt.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;

@SpringBootTest
@Slf4j
public class ShardingJdbcTest {

    @Autowired
    private IOrderMapper mapper;

    /**
     * 新增订单
     */
    @Test
    public void addOrder() {
        for (int i = 0; i < 100; i++) {
            OrderPo orderPo = JMockData.mock(OrderPo.class);
            orderPo.setOrderId((long) i);
            orderPo.setStatus(1);
            orderPo.setDeleted(0);
            Faker faker = new Faker();
            Calendar fromDate = Calendar.getInstance();
            fromDate.set(2023, Calendar.JANUARY, 1);
            Calendar toDate = Calendar.getInstance();
            toDate.set(2023, Calendar.DECEMBER, 31);
            orderPo.setOrderDate(faker.date().between(fromDate.getTime(), toDate.getTime()));
            log.info("add new order:{}", orderPo);
            mapper.insert(orderPo);
        }
    }

    /**
     * 单个查询
     */
    @Test
    public void queryOrder_1() {
        QueryWrapper<OrderPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_date", DateUtil.parseDate("2023-03-12", DateUtil.DATE_PATTERN_2));
        List<OrderPo> orderPos = mapper.selectList(queryWrapper);
        log.info("orderPos={}", JSON.toJSONString(orderPos));
    }

    /**
     * 范围查询
     */
    @Test
    public void queryOrder_2() {
        QueryWrapper<OrderPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("order_date", DateUtil.parseDate("2023-05-01", DateUtil.DATE_PATTERN_2), DateUtil.parseDate("2023-08-01", DateUtil.DATE_PATTERN_2));
        List<OrderPo> orderPos = mapper.selectList(queryWrapper);
        log.info("orderPos={}", JSON.toJSONString(orderPos));
    }
}

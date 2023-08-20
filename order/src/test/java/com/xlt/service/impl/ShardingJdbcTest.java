package com.xlt.service.impl;


import com.github.javafaker.Faker;
import com.github.jsonzou.jmockdata.JMockData;
import com.xlt.mapper.IOrderMapper;
import com.xlt.model.po.OrderPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
@Slf4j
public class ShardingJdbcTest {

    @Autowired
    private IOrderMapper mapper;

    @Test
    public void addOrder() {
        for (int i = 0; i < 10; i++) {
            OrderPo orderPo = JMockData.mock(OrderPo.class);
            orderPo.setStatus(1);
            orderPo.setDeleted(0);
            Faker faker = new Faker();
            Calendar fromDate = Calendar.getInstance();
            fromDate.set(2022, Calendar.JANUARY, 1);
            Calendar toDate = Calendar.getInstance();
            toDate.set(2032, Calendar.DECEMBER, 31);
            orderPo.setOrderDate(faker.date().between(fromDate.getTime(), toDate.getTime()));
            mapper.insert(orderPo);
        }
    }
}

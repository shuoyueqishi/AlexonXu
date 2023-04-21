package com.xlt.service.impl;

import com.alexon.authorization.utils.ObjectUtil;
import com.alexon.model.response.DataResponse;
import com.xlt.model.mapper.OrderConvertor;
import com.xlt.model.po.OrderPo;
import com.xlt.model.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("查询订单列表数据")
    void queryOrderList() {
        DataResponse<List<OrderVo>> response = orderService.queryOrderList(new OrderVo());
        Assertions.assertNotNull(response,"response is null");
        Assertions.assertNotNull(response.getData(),"data is null");
        log.info("data.size()={}",response.getData().size());
    }

    @Test
    @DisplayName("比较mapstruct和dozer转换的效率")
    void compareMapPerformance() {
        DataResponse<List<OrderVo>> response = orderService.queryOrderList(new OrderVo());
        Assertions.assertNotNull(response,"response is null");
        List<OrderVo> orderVos = response.getData();
        log.info("orderVos.size={}",orderVos.size());
        long startMs = System.currentTimeMillis();
        List<OrderPo> orderPoList = ObjectUtil.convertObjsList(orderVos, OrderPo.class);
        long t1 = System.currentTimeMillis() - startMs;
        log.info("converted by dozer,time cost time t1={}ms,orderVos.size={}",t1,orderVos.size());

        startMs=System.currentTimeMillis();
        List<OrderPo> orderPos = OrderConvertor.INSTANCE.convert2OrderPos(orderVos);
        long t2 = System.currentTimeMillis() - startMs;
        log.info("converted by mapstruct,time cost time t2={}ms,orderVos.size={}",t2,orderVos.size());

        Float fT1 = (float) t1;
        Float fT2 = (float) t2;
        log.info("t2/t1*100%={}%",fT2/fT1*100);
        Assertions.assertFalse(t1<t2,"t1<t2");
    }

}

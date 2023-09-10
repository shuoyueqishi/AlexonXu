package com.xlt.service.impl;

import com.alexon.authorization.constants.CommConstant;
import com.alexon.authorization.constants.RedisConstant;
import com.alexon.authorization.utils.ObjectConvertUtil;
import com.alexon.authorization.utils.PoUtil;
import com.alexon.distributed.id.SeqNoGenUtil;
import com.alexon.limiter.utils.RedisUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlt.mapper.IOrderCommodityMapper;
import com.xlt.mapper.IOrderMapper;
import com.xlt.mapper.IReceiverInfoMapper;
import com.xlt.model.mapper.OrderConvertor;
import com.xlt.model.po.OrderCommodityPo;
import com.xlt.model.po.OrderPo;
import com.xlt.model.po.ReceiverInfoPo;
import com.xlt.model.vo.OrderCommodityVo;
import com.xlt.model.vo.OrderVo;
import com.xlt.model.vo.StockVo;
import com.xlt.mq.sender.MqSender;
import com.xlt.service.IOrderService;
import com.xlt.service.feign.ICommodityFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrderService implements IOrderService {

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IOrderCommodityMapper orderCommodityMapper;

    @Autowired
    private IReceiverInfoMapper receiverInfoMapper;

    @Autowired
    private ICommodityFeignClient commodityFeignClient;

    @Autowired
    private MqSender mqSender;

    @Override
    @Transactional
    public BasicResponse createOrder(OrderVo orderVo) {
        BasicResponse checkResult = checkOrderParams(orderVo);
        if (checkResult != null) return checkResult;
        // 先创建订单头，再创建明细
        String orderNo = SeqNoGenUtil.getSeqNoWithTime(CommConstant.ORDER_PREFIX,"yyyyMMdd", 8, Duration.ofDays(7));
        orderVo.setOrderNo(orderNo);
        createRealOrder(orderVo);
        return new BasicResponse("Submit order success, orderNo is:"+orderNo);
    }

    public void createRealOrder(OrderVo orderVo) {
        OrderPo orderPo = ObjectConvertUtil.convertObjs(orderVo, OrderPo.class);
        PoUtil.buildCreateUserInfo(orderPo);
        orderMapper.insert(orderPo);
        List<OrderCommodityPo> orderCommodityPos = new ArrayList<>();
        List<StockVo> stockVoList = new ArrayList<>();
        orderVo.getCommodityList().forEach(orderComVo->{
            orderComVo.setOrderId(orderPo.getOrderId()); // 插入之后自动生成orderID
            OrderCommodityPo orderCommodityPo = ObjectConvertUtil.convertObjs(orderComVo, OrderCommodityPo.class);
            PoUtil.buildCreateUserInfo(orderCommodityPo);
            orderCommodityPos.add(orderCommodityPo);
            StockVo stockVo = StockVo.builder().skuId(orderComVo.getSkuId()).quantity(orderComVo.getQuantity()).build();
            stockVoList.add(stockVo);
        });
        orderCommodityMapper.batchInsert(orderCommodityPos);
        BasicResponse updResponse = commodityFeignClient.updateStockList(stockVoList);
        AssertUtil.isTrue(!updResponse.isSuccess(),"update stock failed");
    }


    private BasicResponse checkOrderParams(OrderVo orderVo) {
        AssertUtil.isNull(orderVo, "orderVo can't be empty");
        AssertUtil.isNull(orderVo.getUserId(), "userId can't by empty");
        AssertUtil.isNull(orderVo.getReceiverId(), "receiverId can't empty");
        // 校验receiver在不在系统中
        Object cachedReceiver = RedisUtil.get(RedisConstant.RECEIVER_INFO + orderVo.getReceiverId());
        if(Objects.isNull(cachedReceiver)) {
            ReceiverInfoPo receiverInfoPo = receiverInfoMapper.selectById(orderVo.getReceiverId());
            AssertUtil.isNull(receiverInfoPo,"receiverId not exist");
            RedisUtil.set(RedisConstant.RECEIVER_INFO + orderVo.getReceiverId(),receiverInfoPo);
        }
        AssertUtil.isNull(orderVo.getOrderAmount(), "orderAmount can't be empty");
        AssertUtil.isNull(orderVo.getDeliveryAmount(), "deliveryAmount can't be empty");
        AssertUtil.isNull(orderVo.getTotalAmount(), "totalAmount can't be empty");
        AssertUtil.isTrue(!orderVo.getTotalAmount().equals(orderVo.getOrderAmount().add(orderVo.getDeliveryAmount())),
                "orderAmount+deliveryAmount != totalAmount");
        AssertUtil.isCollectionEmpty(orderVo.getCommodityList(), "commodityList can't be empty");
        for (OrderCommodityVo orderCommodityVo : orderVo.getCommodityList()) {
            AssertUtil.isNull(orderCommodityVo.getSkuId(), "skuId can't be empty");
            AssertUtil.isTrue(Objects.isNull(orderCommodityVo.getQuantity()) || orderCommodityVo.getQuantity() < 1,
                    "quantity can't be empty or less than 1");
            // 缓存中查询库存是否充足
            Long retCode = preDecrementStockInCache(orderCommodityVo);
            if(retCode==-2){
                return BasicResponse.errorWithMsg("no stock exist");
            } else if(retCode==-1) {
                return BasicResponse.errorWithMsg("stock is not enough");
            }
        }
        return null;
    }

    @Override
    public DataResponse<String> asyncCreateOrder(OrderVo orderVo) {
        BasicResponse checkResult = checkOrderParams(orderVo);
        if (checkResult != null) {
            return ObjectConvertUtil.convertObjs(checkResult, DataResponse.class);
        }
        String orderNo = SeqNoGenUtil.getSeqNoWithTime(CommConstant.ORDER_PREFIX, 8);
        orderVo.setOrderNo(orderNo);
        mqSender.send("directExchange","order", orderVo);
        return new DataResponse<>(orderNo);
    }

    private Long preDecrementStockInCache(OrderCommodityVo orderCommodityVo) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/preDecrementStock.lua")));
        List<String> keyList = new ArrayList<>();
        String key = RedisConstant.SKI_ID+orderCommodityVo.getSkuId();
        keyList.add(key);
        return RedisUtil.redisTemplate.opsForValue().getOperations().execute(redisScript, keyList, orderCommodityVo.getQuantity());
    }

    @Override
    public BasicResponse deleteOrder(Long id) {
        return null;
    }

    @Override
    public BasicResponse updateOrder(OrderVo orderVo) {
        return null;
    }

    @Override
    public DataResponse<List<OrderVo>> queryOrderList(OrderVo orderVo) {
        QueryWrapper<OrderPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(orderVo.getOrderId()),"order_id",orderVo.getOrderId());
        queryWrapper.eq(StringUtils.isNotEmpty(orderVo.getOrderNo()),"order_no",orderVo.getOrderNo());
        List<OrderPo> orderPos = orderMapper.selectList(queryWrapper);

        long startMs=System.currentTimeMillis();
        List<OrderVo> orderVos = OrderConvertor.INSTANCE.convert2OrderVos(orderPos);
        log.info("converted by mapstruct,time cost={}ms",System.currentTimeMillis()-startMs);
        return new DataResponse<>(orderVos);
    }
}

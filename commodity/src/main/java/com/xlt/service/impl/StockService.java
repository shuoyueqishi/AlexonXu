package com.xlt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.constant.RedisConstant;
import com.xlt.exception.CommonException;
import com.xlt.mapper.IStockMapper;
import com.xlt.model.po.StockPo;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.StockVo;
import com.xlt.service.IStockService;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.PoUtil;
import com.xlt.utils.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StockService implements IStockService {

    @Autowired
    private IStockMapper stockMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public PagedResponse<List<StockVo>> queryStockPageList(StockVo stockVo, PageVo pageVo) {
        QueryWrapper<StockPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(stockVo.getSkuId()), "skuId", stockVo.getSkuId());
        queryWrapper.eq(Objects.nonNull(stockVo.getId()), "id", stockVo.getId());
        queryWrapper.eq(Objects.nonNull(stockVo.getQuantity()), "quantity", stockVo.getQuantity());
        Page<StockPo> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());
        Page<StockPo> stockPoPage = stockMapper.selectPage(page, queryWrapper);
        for (StockPo stockPo : stockPoPage.getRecords()) {
            RedisUtil.set(RedisConstant.SKI_ID + stockPo.getSkuId(), stockPo.getQuantity());
        }
        List<StockVo> stockVos = ObjectUtil.convertObjsList(stockPoPage.getRecords(), StockVo.class);
        pageVo.setTotalPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        return new PagedResponse<>(stockVos, pageVo);
    }

    @Override
    public BasicResponse createStock(StockVo stockVo) {
        log.info("create stock info:{}", stockVo);
        AssertUtil.isNull(stockVo.getSkuId(), "skuId can't be empty");
        AssertUtil.isNull(stockVo.getQuantity(), "quantity can't be empty");
        AssertUtil.isTrue(stockVo.getQuantity() <= 0, "quantity should bigger than zero");
        QueryWrapper<StockPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("skuId", stockVo.getSkuId());
        Integer count = stockMapper.selectCount(queryWrapper);
        AssertUtil.isTrue(count > 0, "skuId:" + stockVo.getSkuId() + " not exists");
        StockPo stockPo = ObjectUtil.convertObjs(stockVo, StockPo.class);
        PoUtil.buildCreateUserInfo(stockPo);
        stockMapper.insert(stockPo);
        RedisUtil.set(RedisConstant.SKI_ID + stockPo.getSkuId(), stockPo.getQuantity());
        return new BasicResponse();
    }

    @Override
    public BasicResponse updateStock(StockVo stockVo) {
        log.info("update stock info:{}", stockVo);
        AssertUtil.isNull(stockVo.getSkuId(), "skuId can't be empty");
        RLock fairLock = redissonClient.getFairLock(stockVo.getSkuId() + "");
        try {
            if (fairLock.tryLock(3, 2, TimeUnit.SECONDS)) {
                QueryWrapper<StockPo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sku_id", stockVo.getSkuId());
                StockPo stockPo = stockMapper.selectOne(queryWrapper);
                AssertUtil.isNull(stockPo, "skuId:" + stockVo.getSkuId() + " not exists");
                AssertUtil.isTrue(stockPo.getQuantity() < stockVo.getQuantity(),
                        "stock not enough,current stock is:" + stockPo.getQuantity());
                StockPo udpStockPo = StockPo.builder().skuId(stockVo.getSkuId()).quantity(stockVo.getQuantity()).build();
                PoUtil.buildUpdateUserInfo(udpStockPo);
                stockMapper.updateStock(udpStockPo);
                log.info("update stock success for skuId:" + stockVo.getSkuId());
            } else {
                log.error("get redisson lock failed");
                throw new CommonException("get redisson lock failed");
            }
        } catch (InterruptedException e) {
            log.error("update stock error:", e);
            throw new CommonException("update stock error:" + e.getMessage());
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
            }
        }
        return new BasicResponse();
    }

    @Override
    @Transactional
    public BasicResponse updateStockList(List<StockVo> stockVoList) {
        log.info("stockVoList.size={}", stockVoList.size());
        for (StockVo stockVo : stockVoList) {
            updateStock(stockVo);
        }
        return new BasicResponse();
    }

    @Override
    public BasicResponse deleteStock(Long id) {
        AssertUtil.isNull(id, "id can't be empty");
        StockPo stockPo = stockMapper.selectById(id);
        AssertUtil.isNull(stockPo, "id:" + id + " not exists");
        stockMapper.deleteById(id);
        RedisUtil.delete(RedisConstant.SKI_ID + stockPo.getSkuId());
        return new BasicResponse();
    }
}

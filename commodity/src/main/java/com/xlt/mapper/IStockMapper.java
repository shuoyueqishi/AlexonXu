package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.BrandPo;
import com.xlt.model.po.StockPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface IStockMapper extends BaseMapper<StockPo> {

    int updateStock(@Param("po") StockPo stockPo);

}

package com.xlt.service.feign;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.StockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("commodity")
public interface ICommodityFeignClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/commodity/stock/update/list",headers = "internal=order")
    BasicResponse updateStockList(@RequestBody List<StockVo> stockVoList);
}

package com.xlt.controller;

import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.StockVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private IStockService stockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.CREATE, operateDesc = "create stock")
    BasicResponse addStock(@RequestBody StockVo stockVo) {
        return stockService.createStock(stockVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.DELETE, operateDesc = "delete stock")
    BasicResponse deleteStock(@PathVariable("id") Long id) {
        return stockService.deleteStock(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.UPDATE, operateDesc = "update stock")
    BasicResponse updateStock(@RequestBody StockVo stockVo) {
        return stockService.updateStock(stockVo);
    }

    @RequestMapping(value = "/update/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.UPDATE, operateDesc = "update stock list")
    BasicResponse updateStockList(@RequestBody List<StockVo> stockVoList) {
        return stockService.updateStockList(stockVoList);
    }

    @RequestMapping(value = "/query/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.READ, operateDesc = "query stock paged list")
    BasicResponse queryStockList(@QueryParam("stockVo") StockVo stockVo, @PathVariable("pageSize") int pageSize,
                                 @PathVariable("curPage") int curPage) {
        return stockService.queryStockPageList(stockVo, PageVo.builder().pageSize(pageSize).currentPage(curPage).build());
    }
}


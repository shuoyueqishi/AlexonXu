package com.xlt.controller;

import com.xlt.annotation.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.StockVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.IBrandService;
import com.xlt.service.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private IStockService stockService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.CREATE, operateDesc = "create stock")
    BasicResponse addBrand(@RequestBody StockVo stockVo) {
        return stockService.createStock(stockVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.DELETE, operateDesc = "delete stock")
    BasicResponse deleteBrand(@PathVariable("id") Long id) {
        return stockService.deleteStock(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.UPDATE, operateDesc = "update stock")
    BasicResponse updateBrand(@RequestBody StockVo stockVo) {
        return stockService.updateStock(stockVo);
    }

    @RequestMapping(value = "/query/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = "application/json")
    @OperatePermission(resourceName = "StockController", operateCode = OperateConstant.READ, operateDesc = "query stock paged list")
    BasicResponse queryBrandList(@QueryParam("stockVo") StockVo stockVo, @PathVariable("pageSize") int pageSize,
                                 @PathVariable("curPage") int curPage) {
        return stockService.queryStockPageList(stockVo, PageVo.builder().pageSize(pageSize).currentPage(curPage).build());
    }
}


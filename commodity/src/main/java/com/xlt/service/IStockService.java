package com.xlt.service;


import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.StockVo;

import java.util.List;

public interface IStockService {

    PagedResponse<List<StockVo>> queryStockPageList(StockVo stockVo, PageVo pageVo);

    BasicResponse createStock(StockVo stockVo);

    BasicResponse updateStock(StockVo stockVo);

    BasicResponse deleteStock(Long id);

}

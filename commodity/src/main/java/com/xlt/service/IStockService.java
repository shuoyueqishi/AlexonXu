package com.xlt.service;


import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.xlt.model.vo.StockVo;

import java.util.List;

public interface IStockService {

    PagedResponse<List<StockVo>> queryStockPageList(StockVo stockVo, PageVo pageVo);

    BasicResponse createStock(StockVo stockVo);

    BasicResponse updateStock(StockVo stockVo);

    BasicResponse updateStockList(List<StockVo> stockVoList);

    BasicResponse deleteStock(Long id);

}

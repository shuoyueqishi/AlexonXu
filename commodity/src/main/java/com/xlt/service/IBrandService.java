package com.xlt.service;


import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.PagedResponse;
import com.xlt.model.vo.BrandVo;
import com.xlt.model.vo.PageVo;

import java.util.List;

public interface IBrandService {

    PagedResponse<List<BrandVo>> queryBrandPageList(BrandVo brandVo, PageVo pageVo);

    BasicResponse createBrand(BrandVo brandVo);

    BasicResponse updateBrand(BrandVo brandVo);

    BasicResponse deleteBrand(Long id);

}

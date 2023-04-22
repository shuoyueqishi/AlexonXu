package com.xlt.service;


import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.xlt.model.vo.BrandVo;

import java.util.List;

public interface IBrandService {

    PagedResponse<List<BrandVo>> queryBrandPageList(BrandVo brandVo, PageVo pageVo);

    BasicResponse createBrand(BrandVo brandVo);

    BasicResponse updateBrand(BrandVo brandVo);

    BasicResponse deleteBrand(Long id);

}

package com.xlt.controller;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.BrandVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    BasicResponse addBrand(@RequestBody BrandVo brandVo) {
        return brandService.createBrand(brandVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    BasicResponse deleteBrand(@PathVariable("id") Long id) {
        return brandService.deleteBrand(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
    BasicResponse updateBrand(@RequestBody BrandVo brandVo) {
        return brandService.updateBrand(brandVo);
    }

    @RequestMapping(value = "/query/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = "application/json")
    BasicResponse queryBrandList(@QueryParam("brandVo") BrandVo brandVo, @PathVariable("pageSize") int pageSize,
                                 @PathVariable("curPage") int curPage) {
        return brandService.queryBrandPageList(brandVo, PageVo.builder().pageSize(pageSize).currentPage(curPage).build());
    }
}

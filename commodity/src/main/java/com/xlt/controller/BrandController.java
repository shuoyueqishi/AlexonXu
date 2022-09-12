package com.xlt.controller;

import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.vo.BrandVo;
import com.xlt.model.vo.PageVo;
import com.xlt.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "BrandController",operateCode = OperateConstant.CREATE, operateDesc = "create brand")
    BasicResponse addBrand(@RequestBody BrandVo brandVo) {
        return brandService.createBrand(brandVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "BrandController",operateCode =OperateConstant.DELETE, operateDesc = "delete brand")
    BasicResponse deleteBrand(@PathVariable("id") Long id) {
        return brandService.deleteBrand(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "BrandController",operateCode =OperateConstant.UPDATE, operateDesc = "update brand")
    BasicResponse updateBrand(@RequestBody BrandVo brandVo) {
        return brandService.updateBrand(brandVo);
    }

    @RequestMapping(value = "/query/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "BrandController",operateCode =OperateConstant.READ, operateDesc = "query brand paged list")
    BasicResponse queryBrandList(@QueryParam("brandVo") BrandVo brandVo, @PathVariable("pageSize") int pageSize,
                                 @PathVariable("curPage") int curPage) {
        return brandService.queryBrandPageList(brandVo, PageVo.builder().pageSize(pageSize).currentPage(curPage).build());
    }
}


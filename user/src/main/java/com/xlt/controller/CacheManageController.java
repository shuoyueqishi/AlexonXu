package com.xlt.controller;

import com.alexon.cache.model.request.CacheMgtRequest;
import com.alexon.cache.service.ICacheMgtService;
import com.alexon.model.response.BasicResponse;
import com.alexon.operation.log.OperationLog;
import com.alexon.operation.log.constants.OperateConstant;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/cache/mgt")
public class CacheManageController {

    @Autowired
    private ICacheMgtService cacheMgtService;

    @RequestMapping(value = "/evict", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("evict cache")
    @OperationLog(operateModule = "Cache Manager", operateType = OperateConstant.DELETE, operateDesc = "evict cache")
    BasicResponse evictCache(@RequestBody CacheMgtRequest request) {
        cacheMgtService.evictCache(request);
        return new BasicResponse();
    }

    @RequestMapping(value = "/clear/{cacheName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("clear cache")
    @OperationLog(operateModule = "Cache Manager", operateType = OperateConstant.DELETE, operateDesc = "clear cache")
    BasicResponse clearCache(@PathVariable("cacheName") String cacheName) {
        cacheMgtService.clearCache(CacheMgtRequest.builder().cacheName(cacheName).build());
        return new BasicResponse();
    }
}

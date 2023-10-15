package com.xlt.controller;

import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.operation.log.OperationLog;
import com.alexon.operation.log.constants.OperateConstant;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.service.ISystemConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/system/config")
public class SystemConfigController {

    @Autowired
    private ISystemConfigService systemConfigService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("create system configuration")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.CREATE, operateDesc = "create system configuration")
    BasicResponse createSystemConfig(@RequestBody SystemConfigVo systemConfigVo) {
        return systemConfigService.createSystemConfig(systemConfigVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query system configuration list")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.READ, operateDesc = "query system configuration list")
    DataResponse<List<SystemConfigVo>> querySystemConfigList(@QueryParam("") SystemConfigVo systemConfigVo) {
        List<SystemConfigVo> systemConfigVos = systemConfigService.querySysParams(systemConfigVo);
        return new DataResponse(systemConfigVos);
    }

    @RequestMapping(value = "/query/page/list/{pageSize}/{currentPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query system configuration paged list")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.READ, operateDesc = "query system configuration paged list")
    PagedResponse<List<SystemConfigVo>> querySystemConfigPageList(@QueryParam("") SystemConfigVo systemConfigVo, @PathVariable("pageSize") int pageSize,
                                                   @PathVariable("currentPage") int currentPage) {
        return systemConfigService.querySysParamPageList(systemConfigVo,pageSize,currentPage);
    }

    @RequestMapping(value = "/query/single/{configCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query system configuration by id")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.READ, operateDesc = "query system configuration by id")
    DataResponse<SystemConfigVo> querySystemConfigSingle(@PathVariable("configCode") String configCode) {
        SystemConfigVo systemConfigVo = systemConfigService.querySysParamByCode(configCode);
        return new DataResponse<>(systemConfigVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("update system configuration")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.UPDATE, operateDesc = "update system configuration")
    BasicResponse updateSystemConfig(@RequestBody SystemConfigVo systemConfigVo) {
        systemConfigService.updateSysParam(systemConfigVo);
        return new BasicResponse();
    }

    @RequestMapping(value = "/delete/{configCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("delete system configuration")
    @OperationLog(operateModule = "System Configuration", operateType = OperateConstant.DELETE, operateDesc = "delete system configuration")
    BasicResponse deleteSystemConfig(@PathVariable("configCode") String configCode) {
        systemConfigService.deleteSysConfig(configCode);
        return new BasicResponse();
    }

}

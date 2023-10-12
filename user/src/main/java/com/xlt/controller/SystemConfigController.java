package com.xlt.controller;

import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/system/config")
public class SystemConfigController {

    @Autowired
    private ISystemConfigService systemConfigService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    BasicResponse createSystemConfig(@RequestBody SystemConfigVo systemConfigVo) {
        return systemConfigService.createSystemConfig(systemConfigVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    DataResponse<List<SystemConfigVo>> querySystemConfigList(@RequestBody SystemConfigVo systemConfigVo) {
        List<SystemConfigVo> systemConfigVos = systemConfigService.querySysParams(systemConfigVo);
        return new DataResponse(systemConfigVos);
    }

    @RequestMapping(value = "/query/single/{configId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    DataResponse<SystemConfigVo> querySystemConfigSingle(@PathVariable("configId") Long configId) {
        SystemConfigVo systemConfigVo = systemConfigService.querySysParamById(configId);
        return new DataResponse<>(systemConfigVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    BasicResponse updateSystemConfig(@RequestBody SystemConfigVo systemConfigVo) {
        systemConfigService.updateSysParam(systemConfigVo);
        return new BasicResponse();
    }

    @RequestMapping(value = "/delete/{configId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    BasicResponse deleteSystemConfig(@PathVariable("configId") Long configId) {
        systemConfigService.deleteSysParam(configId);
        return new BasicResponse();
    }

}

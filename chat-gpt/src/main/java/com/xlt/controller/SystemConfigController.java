package com.xlt.controller;

import com.alexon.model.response.BasicResponse;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}

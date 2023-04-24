package com.xlt.service.impl;

import com.alexon.authorization.utils.PoUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alibaba.fastjson.JSON;
import com.xlt.model.conventors.SystemConfigConvertor;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.mapper.SystemConfigMapper;
import com.xlt.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemConfigService implements ISystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public BasicResponse createSystemConfig(SystemConfigVo systemConfigVo) {
        log.info("create input info:{}", systemConfigVo);
        AssertUtil.isNull(systemConfigVo, "systemConfigVo is empty");
        AssertUtil.isStringEmpty(systemConfigVo.getConfigCode(), "configCode can't empty");
        AssertUtil.isNull(systemConfigVo.getValue(), "value can't empty");
        SystemConfigPo systemConfigPo = SystemConfigConvertor.INSTANCE.toSystemConfigPo(systemConfigVo);
        systemConfigPo.setValue(JSON.toJSONString(systemConfigPo.getValue()));
        systemConfigPo.setDeleted(0);
        systemConfigPo.setStatus(1);
        PoUtil.buildCreateUserInfo(systemConfigPo);
        systemConfigMapper.insert(systemConfigPo);
        return new BasicResponse("create success for:" + systemConfigVo.getConfigCode());
    }
}

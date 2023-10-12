package com.xlt.service.impl;

import com.alexon.authorization.utils.PoUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlt.mapper.SystemConfigMapper;
import com.xlt.model.convertor.SystemConfigConvertor;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    @Cacheable(value="SystemParams",keyGenerator = "rcCacheKeyGenerator")
    public List<SystemConfigVo> querySysParams(SystemConfigVo systemConfigVo) {
        log.info("querySysParams condition={}",systemConfigVo);
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_id",systemConfigVo.getConfigId());
        queryWrapper.like("config_code",systemConfigVo.getConfigCode());
        queryWrapper.eq("status",systemConfigVo.getStatus());
        List<SystemConfigPo> systemConfigPos = systemConfigMapper.selectList(queryWrapper);
        return SystemConfigConvertor.INSTANCE.toSystemConfigVos(systemConfigPos);
    }


    @Override
    @Cacheable(value = "SystemParams", key = "#configId")
    public SystemConfigVo querySysParamById(Long configId) {
        log.info("querySysParamById configId={}",configId);
        SystemConfigPo systemConfigPo = systemConfigMapper.selectById(configId);
        return SystemConfigConvertor.INSTANCE.toSystemConfigVo(systemConfigPo);
    }

    @Override
    @CachePut(value = "SystemParams", key = "#systemConfigVo.configId")
    public SystemConfigVo updateSysParam(SystemConfigVo systemConfigVo) {
        log.info("update info={}",systemConfigVo);
        AssertUtil.isNull(systemConfigVo.getConfigId(),"configId can't be empty");
        SystemConfigPo existed = systemConfigMapper.selectById(systemConfigVo.getConfigId());
        AssertUtil.isNull(existed,"configId:"+systemConfigVo.getConfigId()+" isn't exist");
        SystemConfigPo configPo = SystemConfigConvertor.INSTANCE.toSystemConfigPo(systemConfigVo);
        systemConfigMapper.updateSysParamById(configPo);
        SystemConfigPo updPo = systemConfigMapper.selectById(systemConfigVo.getConfigId());
        return SystemConfigConvertor.INSTANCE.toSystemConfigVo(updPo);
    }

    @Override
    @CacheEvict(value = "SystemParams", key = "#configId")
    public void deleteSysParam(Long configId) {
       log.info("delete configId={}",configId);
       systemConfigMapper.deleteById(configId);
    }
}

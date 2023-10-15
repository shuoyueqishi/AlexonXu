package com.xlt.service.impl;

import com.alexon.authorization.utils.PoUtil;
import com.alexon.authorization.utils.VoUtil;
import com.alexon.exception.utils.AssertUtil;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.utils.ObjectUtil;
import com.alexon.model.vo.PageVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.enums.DeleteEnum;
import com.xlt.enums.SystemConfigTypeEnum;
import com.xlt.mapper.SystemConfigMapper;
import com.xlt.model.convertor.SystemConfigConvertor;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import com.xlt.service.ISystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class SystemConfigService implements ISystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public BasicResponse createSystemConfig(@Valid SystemConfigVo systemConfigVo) {
        log.info("create input info:{}", systemConfigVo);
        AssertUtil.isNull(systemConfigVo, "systemConfigVo is empty");
        AssertUtil.isStringEmpty(systemConfigVo.getConfigCode(), "configCode can't empty");
        AssertUtil.isNull(systemConfigVo.getValue(), "value can't empty");
        AssertUtil.isNull(systemConfigVo.getConfigType(), "configType can't empty");
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_code", systemConfigVo.getConfigCode());
        queryWrapper.eq("deleted", DeleteEnum.NOT_DELETE.getCode());
        Integer count = systemConfigMapper.selectCount(queryWrapper);
        AssertUtil.isTrue(count > 0, "config_code:" + systemConfigVo.getConfigCode() + " exists in system");
        SystemConfigPo systemConfigPo = SystemConfigConvertor.INSTANCE.toSystemConfigPo(systemConfigVo);
        if (SystemConfigTypeEnum.JSON.getCode().equals(systemConfigVo.getConfigType())) {
            systemConfigPo.setValue(JSON.toJSONString(systemConfigPo.getValue()));
        }
        systemConfigPo.setDeleted(DeleteEnum.NOT_DELETE.getCode());
        PoUtil.buildCreateUserInfo(systemConfigPo);
        systemConfigMapper.insert(systemConfigPo);
        return new BasicResponse("create success for:" + systemConfigVo.getConfigCode());
    }

    @Override
    @Cacheable(value = "SystemParams", keyGenerator = "rcCacheKeyGenerator")
    public List<SystemConfigVo> querySysParams(SystemConfigVo systemConfigVo) {
        log.info("querySysParams condition={}", systemConfigVo);
        QueryWrapper<SystemConfigPo> queryWrapper = getQueryCondition(systemConfigVo);
        List<SystemConfigPo> systemConfigPos = systemConfigMapper.selectList(queryWrapper);
        List<SystemConfigVo> systemConfigVos = SystemConfigConvertor.INSTANCE.toSystemConfigVos(systemConfigPos);
        systemConfigVos.forEach(vo -> {
            vo.setConfigTypeDesc(SystemConfigTypeEnum.getDescByCode(vo.getConfigType()));
            vo.setDeletedDesc(DeleteEnum.getDescByCode(vo.getDeleted()));
        });
        VoUtil.fillUserNames(systemConfigVos);
        return systemConfigVos;
    }

    @Override
    @Cacheable(value = "SystemParams", keyGenerator = "rcCacheKeyGenerator")
    public PagedResponse<List<SystemConfigVo>> querySysParamPageList(SystemConfigVo systemConfigVo, int pageSize, int curPage) {
        log.info("querySysParamPageList condition={}", systemConfigVo);
        QueryWrapper<SystemConfigPo> queryWrapper = getQueryCondition(systemConfigVo);
        Page<SystemConfigPo> page = new Page(curPage, pageSize);
        Page<SystemConfigPo> poPage = systemConfigMapper.selectPage(page, queryWrapper);
        List<SystemConfigVo> systemConfigVos = SystemConfigConvertor.INSTANCE.toSystemConfigVos(poPage.getRecords());
        systemConfigVos.forEach(vo -> {
            vo.setConfigTypeDesc(SystemConfigTypeEnum.getDescByCode(vo.getConfigType()));
            vo.setDeletedDesc(DeleteEnum.getDescByCode(vo.getDeleted()));
        });
        VoUtil.fillUserNames(systemConfigVos);
        PagedResponse<List<SystemConfigVo>> response = new PagedResponse<>();
        response.setData(systemConfigVos);
        response.setPage(PageVo.builder()
                .pageSize(pageSize)
                .currentPage(curPage)
                .total(poPage.getTotal())
                .totalPages(poPage.getPages())
                .build());
        return response;
    }

    private QueryWrapper<SystemConfigPo> getQueryCondition(SystemConfigVo systemConfigVo) {
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(systemConfigVo.getConfigId()),"config_id", systemConfigVo.getConfigId());
        queryWrapper.likeRight(StringUtils.isNotEmpty(systemConfigVo.getConfigCode()),"config_code", systemConfigVo.getConfigCode());
        queryWrapper.like(StringUtils.isNotEmpty(systemConfigVo.getDeletedDesc()),"description", systemConfigVo.getDescription());
        queryWrapper.like(Objects.nonNull(systemConfigVo.getValue()),"value", systemConfigVo.getValue());
        if(Objects.isNull(systemConfigVo.getDeleted())) {
            queryWrapper.eq("deleted", DeleteEnum.NOT_DELETE.getCode());
        } else {
            queryWrapper.eq("deleted", systemConfigVo.getDeleted());
        }
        queryWrapper.orderByDesc("last_update_date");
        return queryWrapper;
    }

    @Override
    @Cacheable(value = "SystemParams", key = "#configCode")
    public SystemConfigVo querySysParamByCode(String configCode) {
        log.info("querySysParamById configCode={}", configCode);
        AssertUtil.isStringEmpty(configCode,"configCode can't be empty");
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_code", configCode);
        queryWrapper.eq("deleted", DeleteEnum.NOT_DELETE.getCode());
        SystemConfigPo systemConfigPo = systemConfigMapper.selectOne(queryWrapper);
        SystemConfigVo systemConfigVo = SystemConfigConvertor.INSTANCE.toSystemConfigVo(systemConfigPo);
        systemConfigVo.setConfigTypeDesc(SystemConfigTypeEnum.getDescByCode(systemConfigVo.getConfigType()));
        systemConfigVo.setDeletedDesc(DeleteEnum.getDescByCode(systemConfigVo.getDeleted()));
        VoUtil.fillUserName(systemConfigVo);
        return systemConfigVo;
    }

    @Override
    @CachePut(value = "SystemParams", key = "#systemConfigVo.configCode")
    public SystemConfigVo updateSysParam(SystemConfigVo systemConfigVo) {
        log.info("update info={}", systemConfigVo);
        AssertUtil.isNull(systemConfigVo.getConfigCode(), "configCode can't be empty");
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_code", systemConfigVo.getConfigCode());
        SystemConfigPo existed = systemConfigMapper.selectOne(queryWrapper);
        AssertUtil.isNull(existed, "configCode:" + systemConfigVo.getConfigCode() + " not exists in system");
        SystemConfigPo configPo = SystemConfigConvertor.INSTANCE.toSystemConfigPo(systemConfigVo);
        systemConfigMapper.updateSysParamByCode(configPo);
        SystemConfigPo updPo = systemConfigMapper.selectById(systemConfigVo.getConfigId());
        SystemConfigVo updatedVo = SystemConfigConvertor.INSTANCE.toSystemConfigVo(updPo);
        updatedVo.setConfigTypeDesc(SystemConfigTypeEnum.getDescByCode(updatedVo.getConfigType()));
        updatedVo.setDeletedDesc(DeleteEnum.getDescByCode(updatedVo.getDeleted()));
        VoUtil.fillUserName(updatedVo);
        return updatedVo;
    }

    @Override
    @CacheEvict(value = "SystemParams", key = "#configCode")
    public void deleteSysConfig(String configCode) {
        log.info("delete configCode={}", configCode);
        AssertUtil.isStringEmpty(configCode,"configCode can't be empty");
        QueryWrapper<SystemConfigPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_code", configCode);
        queryWrapper.eq("deleted", DeleteEnum.NOT_DELETE.getCode());
        SystemConfigPo existed = systemConfigMapper.selectOne(queryWrapper);
        AssertUtil.isNull(existed, "configCode:" + configCode + " not exists in system");
        SystemConfigPo configPo = SystemConfigPo.builder().configCode(configCode).build();
        PoUtil.buildUpdateUserInfo(configPo);
        systemConfigMapper.deleteSystemConfig(configPo);
    }
}

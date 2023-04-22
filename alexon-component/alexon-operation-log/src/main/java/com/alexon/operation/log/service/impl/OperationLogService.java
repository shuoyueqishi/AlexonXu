package com.alexon.operation.log.service.impl;

import com.alexon.authorization.model.vo.OperationLogVo;
import com.alexon.authorization.utils.ObjectConvertUtil;
import com.alexon.authorization.utils.VoUtil;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.alexon.operation.log.mapper.IOperationLogMapper;
import com.alexon.operation.log.model.po.OperationLogPo;
import com.alexon.operation.log.service.IOperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.alexon.authorization.model.vo.OperationLogQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OperationLogService implements IOperationLogService {

    @Autowired
    private IOperationLogMapper optLogMapper;


    /**
     * 分页查询操作日志
     *
     * @param optLogQueryVo 查询条件
     * @param pageVo        分页参数
     * @return 返回值
     */
    @Override
    public PagedResponse<List<OperationLogVo>> queryOperationLogPageList(OperationLogQueryVo optLogQueryVo, PageVo pageVo) {
        log.info("operationLogVo={}", optLogQueryVo);
        QueryWrapper<OperationLogPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(optLogQueryVo.getId()), "id", optLogQueryVo.getId());
        queryWrapper.eq(Objects.nonNull(optLogQueryVo.getCreateBy()), "create_by", optLogQueryVo.getCreateBy());
        queryWrapper.ge(Objects.nonNull(optLogQueryVo.getCreationDateStart()), "creation_date", optLogQueryVo.getCreationDateStart());
        queryWrapper.le(Objects.nonNull(optLogQueryVo.getCreationDateEnd()), "creation_date", optLogQueryVo.getCreationDateEnd());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getUrl()), "url", optLogQueryVo.getUrl());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getOperateModule()), "operate_module", optLogQueryVo.getOperateModule());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getOperateType()), "operate_type", optLogQueryVo.getOperateType());
        queryWrapper.likeRight(StringUtils.isNotEmpty(optLogQueryVo.getUserIp()), "user_ip", optLogQueryVo.getUserIp());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getOperateDesc()), "operate_desc", optLogQueryVo.getOperateDesc());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getRequest()), "request", optLogQueryVo.getRequest());
        queryWrapper.like(StringUtils.isNotEmpty(optLogQueryVo.getResponse()), "response", optLogQueryVo.getResponse());
        queryWrapper.orderByDesc("creation_date");
        Page<OperationLogPo> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());
        Page<OperationLogPo> infoPoPage = optLogMapper.selectPage(page, queryWrapper);
        List<OperationLogVo> operationLogVos = ObjectConvertUtil.convertObjsList(infoPoPage.getRecords(), OperationLogVo.class);
        VoUtil.fillUserNames(operationLogVos);
        pageVo.setTotalPages(page.getPages());
        pageVo.setTotal(page.getTotal());
        return new PagedResponse<>(operationLogVos, pageVo);
    }
}

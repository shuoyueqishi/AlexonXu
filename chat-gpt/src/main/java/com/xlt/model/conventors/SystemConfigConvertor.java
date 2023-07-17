package com.xlt.model.conventors;

import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SystemConfigConvertor {
    SystemConfigConvertor INSTANCE = Mappers.getMapper(SystemConfigConvertor.class);

    SystemConfigVo toSystemConfigVo(SystemConfigPo systemConfigPo);

    List<SystemConfigVo> toSystemConfigVos(List<SystemConfigPo> systemConfigPos);

    SystemConfigPo toSystemConfigPo(SystemConfigVo systemConfigVo);

    List<SystemConfigPo> toSystemConfigPos(List<SystemConfigVo> systemConfigVos);
}

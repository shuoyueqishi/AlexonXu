package com.xlt.model.conventors;

import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SystemConfigConvertor {
    SystemConfigConvertor INSTANCE = Mappers.getMapper(SystemConfigConvertor.class);

    SystemConfigVo toSystemConfigVo(SystemConfigPo systemConfigPo);

    SystemConfigPo toSystemConfigPo(SystemConfigVo systemConfigVo);
}

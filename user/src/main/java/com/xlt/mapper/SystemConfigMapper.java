package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.SystemConfigPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfigPo> {

    int updateSysParamById(@Param("po") SystemConfigPo po);

}

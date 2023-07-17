package com.xlt.service;

import com.alexon.model.response.BasicResponse;
import com.xlt.model.vo.SystemConfigVo;

import java.util.List;

public interface ISystemConfigService {

    BasicResponse createSystemConfig(SystemConfigVo systemConfigVo);

    List<SystemConfigVo> querySysParams(SystemConfigVo systemConfigVo);

    SystemConfigVo querySysParamById(Long configId);

    SystemConfigVo updateSysParam(SystemConfigVo systemConfigV);

    void deleteSysParam(Long configId);
}

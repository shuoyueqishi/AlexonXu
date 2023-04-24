package com.xlt.service;

import com.alexon.model.response.BasicResponse;
import com.xlt.model.vo.SystemConfigVo;

public interface ISystemConfigService {
    BasicResponse createSystemConfig(SystemConfigVo systemConfigVo);
}

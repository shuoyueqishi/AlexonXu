package com.xlt.service;

import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xlt.model.po.SystemConfigPo;
import com.xlt.model.vo.SystemConfigVo;

import java.util.List;

public interface ISystemConfigService {

    BasicResponse createSystemConfig(SystemConfigVo systemConfigVo);

    List<SystemConfigVo> querySysParams(SystemConfigVo systemConfigVo);

    PagedResponse<List<SystemConfigVo>> querySysParamPageList(SystemConfigVo systemConfigVo, int pageSize, int curPage);

    SystemConfigVo querySysParamByCode(String configCode);

    SystemConfigVo updateSysParam(SystemConfigVo systemConfigV);

    void deleteSysConfig(String configCode);
}

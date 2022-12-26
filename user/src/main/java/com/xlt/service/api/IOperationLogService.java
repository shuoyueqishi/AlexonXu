package com.xlt.service.api;

import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.OperationLogQueryVo;
import com.xlt.model.vo.OperationLogVo;
import com.xlt.model.vo.PageVo;

public interface IOperationLogService {
    /**
     * 分页查询操作日志
     *
     * @param operationLogQueryVo 查询条件
     * @param pageVo 分页参数
     * @return 返回值
     */
    PageDataResponse<OperationLogVo> queryOperationLogPageList(OperationLogQueryVo operationLogQueryVo, PageVo pageVo);
}

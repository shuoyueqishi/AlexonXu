package com.alexon.operation.log.service;

import com.alexon.authorization.model.vo.OperationLogVo;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.alexon.authorization.model.vo.OperationLogQueryVo;

import java.util.List;

public interface IOperationLogService {
    /**
     * 分页查询操作日志
     *
     * @param operationLogQueryVo 查询条件
     * @param pageVo 分页参数
     * @return 返回值
     */
    PagedResponse<List<OperationLogVo>> queryOperationLogPageList(OperationLogQueryVo operationLogQueryVo, PageVo pageVo);
}

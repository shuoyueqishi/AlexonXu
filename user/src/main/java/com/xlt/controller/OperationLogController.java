package com.xlt.controller;

import com.alexon.authorization.operate.OperatePermission;
import com.alexon.authorization.model.vo.OperationLogVo;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.alexon.operation.log.constants.OperateConstant;
import com.alexon.authorization.model.vo.OperationLogQueryVo;
import com.alexon.operation.log.service.IOperationLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/operation/log")
@Slf4j
public class OperationLogController {

    @Autowired
    private IOperationLogService operationLogService;

    @RequestMapping(value = "/page/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query operation log paged list")
    @OperatePermission(resourceName = "OperationLogController",operateCode = OperateConstant.READ, operateDesc = "query operation log paged list")
    PagedResponse<List<OperationLogVo>> queryOperationLogPageList(@QueryParam("") OperationLogQueryVo operationLogQueryVo,
                                                                  @PathVariable("pageSize") int pageSize,
                                                                  @PathVariable("curPage") int curPage) {
        PageVo pageVo = PageVo.builder().pageSize(pageSize).currentPage(curPage).build();
        return operationLogService.queryOperationLogPageList(operationLogQueryVo, pageVo);
    }
}

package com.xlt.controller;

import com.alexon.authorization.operate.OperatePermission;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.PagedResponse;
import com.alexon.model.vo.PageVo;
import com.alexon.operation.log.constants.OperateConstant;
import com.xlt.model.vo.ReceiverInfoVo;
import com.xlt.service.IReceiverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/receiver/info")
public class ReceiverInfoController {

    @Autowired
    private IReceiverInfoService receiverInfoService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "ReceiverInfoController", operateCode = OperateConstant.CREATE, operateDesc = "create Receiver Info")
    BasicResponse createReceiverInfo(@RequestBody ReceiverInfoVo receiverInfoVo) {
        return receiverInfoService.createReceiverInfo(receiverInfoVo);
    }

    @RequestMapping(value = "/query/paged/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "ReceiverInfoController", operateCode = OperateConstant.READ, operateDesc = "query Receiver Info paged list")
    PagedResponse<List<ReceiverInfoVo>> queryReceiverInfoPagedList(@QueryParam("receiverInfoVo") ReceiverInfoVo receiverInfoVo,
                                                                   @PathVariable("pageSize") int pageSize,
                                                                   @PathVariable("curPage") int curPage) {
        PageVo pageVo = PageVo.builder().pageSize(pageSize).currentPage(curPage).build();
        return receiverInfoService.queryReceiverInfoPagedList(receiverInfoVo, pageVo);
    }
}


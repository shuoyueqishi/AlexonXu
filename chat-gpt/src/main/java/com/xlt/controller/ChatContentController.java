package com.xlt.controller;


import com.alexon.authorization.operate.OperatePermission;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.response.DataResponse;
import com.alexon.operation.log.OperationLog;
import com.alexon.operation.log.constants.OperateConstant;
import com.xlt.model.request.ChatContentRequest;
import com.xlt.model.request.ChatDeleteRequest;
import com.xlt.model.vo.ChatHeadVo;
import com.xlt.model.vo.ChatLineVo;
import com.xlt.service.IChatService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/chat/content")
public class ChatContentController {

    @Resource
    private IChatService chatService;

    @RequestMapping(value = "/head/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query Chat Head List")
    @OperationLog(operateModule = "ChatContentController", operateType = OperateConstant.READ, operateDesc = "query Chat Head List")
    @OperatePermission(resourceName = "ChatContentController",operateCode =OperateConstant.READ, operateDesc = "query Chat Head List")
    public DataResponse<List<ChatHeadVo>> queryChatHeadList(@QueryParam("") ChatHeadVo chatHeadVo) {
        List<ChatHeadVo> chatHeadVos = chatService.queryChatHeadList(chatHeadVo);
        return new DataResponse<>(chatHeadVos);
    }

    @ApiOperation("query Chat Line List")
    @OperationLog(operateModule = "ChatContentController", operateType = OperateConstant.READ, operateDesc = "query Chat Line List")
    @OperatePermission(resourceName = "ChatContentController",operateCode =OperateConstant.READ, operateDesc = "query Chat Line List")
    @RequestMapping(value = "/line/query", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<List<ChatLineVo>> queryChatLineList(@QueryParam("") ChatLineVo chatLineVo) {
        List<ChatLineVo> chatLineVos = chatService.queryChatLineList(chatLineVo);
        return new DataResponse<>(chatLineVos);
    }

    @ApiOperation("add Chat content")
    @OperationLog(operateModule = "ChatContentController", operateType = OperateConstant.CREATE, operateDesc = "add Chat content")
    @OperatePermission(resourceName = "ChatContentController",operateCode =OperateConstant.CREATE, operateDesc = "add Chat content")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponse addChatContent(@RequestBody ChatContentRequest request) {
        chatService.addChatContent(request);
        return new BasicResponse();
    }

    @ApiOperation("delete Chat content")
    @OperationLog(operateModule = "ChatContentController", operateType = OperateConstant.DELETE, operateDesc = "delete Chat content")
    @OperatePermission(resourceName = "ChatContentController",operateCode =OperateConstant.DELETE, operateDesc = "delete Chat content")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponse deleteChat(@RequestBody @Valid ChatDeleteRequest request) {
        chatService.deleteChat(request.getHeadIdList());
        return new BasicResponse();
    }

    @ApiOperation("update Chat name")
    @OperationLog(operateModule = "ChatContentController", operateType = OperateConstant.UPDATE, operateDesc = "update Chat name")
    @OperatePermission(resourceName = "ChatContentController",operateCode =OperateConstant.UPDATE, operateDesc = "update Chat name")
    @RequestMapping(value = "/name/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicResponse updateChatName(@RequestBody @Valid ChatHeadVo chatHeadVo){
        chatService.updateChatName(chatHeadVo);
        return new BasicResponse();
    }


}

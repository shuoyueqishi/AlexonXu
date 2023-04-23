package com.alexon.authorization.controller;

import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.authorization.utils.PermissionSyncUtil;
import com.alexon.model.response.DataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/permission/sync")
@Api("权限同步")
@Slf4j
public class PermissionSyncController {

    @RequestMapping(value = "/fetch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("同步微服务API接口权限点")
    DataResponse<List<PermissionVo>> fetchApiPermissions(){
        log.info("fetch api permissions...");
        List<PermissionVo> permissionVoList = PermissionSyncUtil.getOperationPermissions();
        return new DataResponse<>(permissionVoList);
    }
}

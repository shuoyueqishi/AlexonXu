package com.xlt.controller;

import com.xlt.ISyncPermissionService;
import com.xlt.annotation.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.logs.OperationLog;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.vo.RolePermissionVo;
import com.xlt.model.vo.UserRoleVo;
import com.xlt.service.api.IPermissionService;
import com.xlt.utils.common.AppContextUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @RequestMapping(value = "/synchronize", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("fetch OperationPermission annotations")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    BasicResponse synchronizePermissions() {
        return permissionService.synchronizePermission();
    }

    @RequestMapping(value = "/synchronize/list", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("fetch OperationPermission annotations")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.EXECUTE, operateDesc = "EXECUTE")
    BasicResponse syncPermissionList(@RequestBody List<PermissionVo> permVoList) {
        ISyncPermissionService syncPermissionService = AppContextUtil.getBean(ISyncPermissionService.class);
        return syncPermissionService.syncPermissionList(permVoList);
    }

    @RequestMapping(value = "/grant/user/role", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("grant roles to user")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.CREATE, operateDesc = "grant roles to user")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.CREATE, operateDesc = "grant roles to user")
    BasicResponse grantRole(@RequestBody List<UserRoleVo> userRoleVoList) {
        return permissionService.grantRoles2User(userRoleVoList);
    }

    @RequestMapping(value = "/user/role/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("query user roles by userId")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query user roles by userId")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query user roles by userId")
    DataResponse<Object> queryUserRoleList(@PathVariable("userId") Long userId) {
        return permissionService.queryUserRoleList(userId);
    }

    @RequestMapping(value = "/grant/role/privilege", method = RequestMethod.POST, produces = "application/json")
    @ApiOperation("grant permissions to role")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.CREATE, operateDesc = "grant permissions to role")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.CREATE, operateDesc = "grant permissions to role")
    BasicResponse grantPermission(@RequestBody RolePermissionVo rolePermissionVo) {
        return permissionService.grantPermissions2Role(rolePermissionVo);
    }

    @RequestMapping(value = "/page/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("query permission paged list")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query permission paged list")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query permission paged list")
    DataResponse<Object> queryPermissionPageList(@QueryParam("")PermissionVo permissionVo,
                                                 @PathVariable("pageSize") int pageSize,
                                                 @PathVariable("curPage") int curPage) {
        PageVo pageVo = PageVo.builder().pageSize(pageSize).currentPage(curPage).build();
        return permissionService.queryPermissionPageList(permissionVo,pageVo);
    }

    @RequestMapping(value = "/list/{roleId}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("query role permission list")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query role permission list")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query role permission list")
    DataResponse<Object> queryRolePermissionList(@PathVariable("roleId") Long roleId) {
        return permissionService.queryRolePermissionList(roleId);
    }
}

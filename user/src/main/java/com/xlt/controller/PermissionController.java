package com.xlt.controller;

import com.github.pagehelper.PageInfo;
import com.xlt.model.po.PermissionPo;
import com.xlt.model.response.PageDataResponse;
import com.xlt.service.ISyncPermissionService;
import com.xlt.auth.OperatePermission;
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
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.VoUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @RequestMapping(value = "/synchronize", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("fetch OperationPermission annotations")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    BasicResponse synchronizePermissions() {
        return permissionService.synchronizePermission();
    }

    @RequestMapping(value = "/synchronize/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("fetch OperationPermission annotations")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.EXECUTE, operateDesc = "synchronize permission")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.EXECUTE, operateDesc = "EXECUTE")
    BasicResponse syncPermissionList(@RequestBody List<PermissionVo> permVoList) {
        ISyncPermissionService syncPermissionService = AppContextUtil.getBean(ISyncPermissionService.class);
        return syncPermissionService.syncPermissionList(permVoList);
    }

    @RequestMapping(value = "/grant/user/role", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("grant roles to user")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.CREATE, operateDesc = "grant roles to user")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.CREATE, operateDesc = "grant roles to user")
    BasicResponse grantRole(@RequestBody List<UserRoleVo> userRoleVoList) {
        return permissionService.grantRoles2User(userRoleVoList);
    }

    @RequestMapping(value = "/user/role/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query user roles by userId")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query user roles by userId")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query user roles by userId")
    DataResponse<Object> queryUserRoleList(@PathVariable("userId") Long userId) {
        return permissionService.queryUserRoleList(userId);
    }

    @RequestMapping(value = "/grant/role/privilege", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("grant permissions to role")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.CREATE, operateDesc = "grant permissions to role")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.CREATE, operateDesc = "grant permissions to role")
    BasicResponse grantPermission(@RequestBody RolePermissionVo rolePermissionVo) {
        return permissionService.grantPermissions2Role(rolePermissionVo);
    }

    @RequestMapping(value = "/remove/role/privilege", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("remove role permissions")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.DELETE, operateDesc = "remove role permissions")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.DELETE, operateDesc = "remove role permissions")
    BasicResponse removeRolePermission(@RequestBody RolePermissionVo rolePermissionVo) {
        return permissionService.removeRolePermission(rolePermissionVo);
    }

    @RequestMapping(value = "/page/list/{pageSize}/{curPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query permission paged list")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query permission paged list")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query permission paged list")
    PageDataResponse<PermissionVo> queryPermissionPageList(@QueryParam("")PermissionVo permissionVo,
                                                     @PathVariable("pageSize") int pageSize,
                                                     @PathVariable("curPage") int curPage) {
        PageVo pageVo = PageVo.builder().pageSize(pageSize).currentPage(curPage).build();
        return permissionService.queryPermissionPageList(permissionVo, pageVo);
    }

    @RequestMapping(value = "/list/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query role permission list")
    @OperationLog(operateModule = "PermissionController", operateType = OperateConstant.READ, operateDesc = "query role permission list")
    @OperatePermission(resourceName = "PermissionController",operateCode =OperateConstant.READ, operateDesc = "query role permission list")
    DataResponse<Object> queryRolePermissionList(@PathVariable("roleId") Long roleId) {
        return permissionService.queryRolePermissionList(roleId);
    }
}

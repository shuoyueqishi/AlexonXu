package com.xlt.controller;

import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.logs.OperationLog;
import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.RoleVo;
import com.xlt.service.api.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;


@RestController
@RequestMapping("/role")
@Api("role management")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("add role")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.CREATE, operateDesc = "Role add")
    @OperatePermission(resourceName = "RoleController",operateCode =OperateConstant.CREATE, operateDesc = "Role add")
    public DataResponse<Object> roleAdd(@RequestBody RoleVo roleVo) {
        return roleService.roleAdd(roleVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("update role")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.UPDATE, operateDesc = "Role update")
    @OperatePermission(resourceName = "RoleController",operateCode =OperateConstant.UPDATE, operateDesc = "Role update")
    public DataResponse<Object> roleUpdate(@RequestBody RoleVo roleVo) {
        return roleService.roleUpdate(roleVo);
    }

    @RequestMapping(value = "/page/list/{pageSize}/{currentPage}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query role paged list")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.READ, operateDesc = "query role paged list")
    @OperatePermission(resourceName = "RoleController",operateCode =OperateConstant.READ, operateDesc = "query role paged list")
    public PageDataResponse<RoleVo> queryRolePageList(@QueryParam("") RoleVo roleVo,
                                                      @PathVariable("pageSize") int pageSize,
                                                      @PathVariable("currentPage") int currentPage) {
        PageVo pageVo = PageVo.builder().pageSize(pageSize).currentPage(currentPage).build();
        return roleService.queryRolePageList(roleVo,pageVo);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("query role list")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.READ, operateDesc = "query role list")
    @OperatePermission(resourceName = "RoleController", operateCode = OperateConstant.READ, operateDesc = "query role list")
    public DataResponse<Object> queryRoleList(@QueryParam("") RoleVo roleVo) {
        return roleService.queryRoleList(roleVo);
    }

    @RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("delete role")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.DELETE, operateDesc = "delete role")
    @OperatePermission(resourceName = "RoleController",operateCode =OperateConstant.DELETE, operateDesc = "delete role")
    public DataResponse<Object> deleteRoleById(@PathVariable("roleId") Long roleId) {
        return roleService.deleteRoleById(roleId);
    }

    @RequestMapping(value = "/change/{roleCode}/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("change role")
    @OperationLog(operateModule = "Role", operateType = OperateConstant.UPDATE, operateDesc = "change role")
//    @OperatePermission(resourceName = "RoleController",operateCode =OperateConstant.UPDATE, operateDesc = "change role")
    public DataResponse<Object> changeRole(@PathVariable("roleCode") String roleCode,@PathVariable("userId")String userId) {
        return roleService.changeRole(roleCode,userId);
    }

}

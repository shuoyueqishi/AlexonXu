package com.xlt.service.api;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.vo.RolePermissionVo;
import com.xlt.model.vo.UserRoleVo;

import java.util.List;

public interface IPermissionService {
    /**
     * 同步权限点
     *
     * @return 返回值
     */
    BasicResponse synchronizePermission();


    /**
     * 给用户授权角色
     *
     * @param userRoleVoList 授权信息
     * @return 返回值
     */
    BasicResponse grantRoles2User(List<UserRoleVo> userRoleVoList);

    /**
     * 根据用户ID查询用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    DataResponse<Object> queryUserRoleList(Long userId);

    /**
     * 给角色授权权限
     *
     * @param rolePermissionVo 授权信息
     * @return 返回值
     */
    BasicResponse grantPermissions2Role(RolePermissionVo rolePermissionVo);

    /**
     * 删除角色的角色
     *
     * @param rolePermissionVo 角色权限信息
     * @return 返回值
     */
    BasicResponse removeRolePermission(RolePermissionVo rolePermissionVo);

    /**
     * 取消用户角色
     *
     * @param userRoleVoList 用户角色信息
     * @return 返回值
     */
    BasicResponse removeRole4User(List<UserRoleVo> userRoleVoList);

    /**
     * 查询权限列表
     *
     * @param permissionVo 查询参数
     * @param pageVo       分页参数
     * @return 返回值
     */
    PageDataResponse<PermissionVo> queryPermissionPageList(PermissionVo permissionVo, PageVo pageVo);

    /**
     * 查询角色权限
     *
     * @param roleId 角色ID
     * @return 权限点列表
     */
    DataResponse<Object> queryRolePermissionList(Long roleId);
}

package com.xlt.service.api;

import com.xlt.model.response.DataResponse;
import com.xlt.model.response.PageDataResponse;
import com.xlt.model.vo.PageVo;
import com.xlt.model.vo.RoleVo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface IRoleService {
    /**
     * 新增用户
     *
     * @param roleVo 用户信息
     * @return 返回值
     */
    DataResponse<Object> roleAdd(@Valid @NotNull RoleVo roleVo);

    /**
     * 更新用户
     *
     * @param roleVo 用户信息
     * @return 返回值
     */
    DataResponse<Object> roleUpdate(@Valid @NotNull RoleVo roleVo);

    /**
     * 分页查询用户信息
     *
     * @param roleVo 用户查询条件
     * @param pageVo 分页信息
     * @return 查询结果
     */
    PageDataResponse<RoleVo> queryRolePageList(RoleVo roleVo, PageVo pageVo);

    /**
     * 查询角色列表
     * @param roleVo 查询参数
     * @return 查询结果
     */
    DataResponse<Object> queryRoleList(RoleVo roleVo);

    /**
     * 删除用户
     *
     * @param roleId 用户id
     * @return 返回值
     */
    DataResponse<Object> deleteRoleById(@Valid @NotNull Long roleId);

    /**
     * 改变角色
     * @param roleCode 角色编码
     * @param userId 用户ID
     * @return 返回角色切换结果
     */
    DataResponse<Object> changeRole(String roleCode,String userId);
}

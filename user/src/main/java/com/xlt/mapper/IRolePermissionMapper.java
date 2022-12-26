package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.RolePermissionPo;
import com.xlt.model.vo.PermissionVo;
import com.xlt.model.vo.RolePermissionVo;
import com.xlt.model.vo.UserRoleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface IRolePermissionMapper extends BaseMapper<RolePermissionPo> {

    int batchInsertPermissions(@Param("list") List<RolePermissionPo> list);

    int removeRolePermission(@Param("vo")RolePermissionVo rolePermissionVo);

    List<PermissionVo> queryPermissionByRoleId(Long roleId);

    List<UserRoleVo> queryRoleListByUserId(Long userId);
}


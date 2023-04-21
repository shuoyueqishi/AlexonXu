package com.alexon.authorization.mapper;

import com.alexon.authorization.model.po.RolePermissionPo;
import com.alexon.authorization.model.vo.PermissionVo;
import com.alexon.authorization.model.vo.RolePermissionVo;
import com.alexon.authorization.model.vo.UserRoleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface IRolePermissionMapper extends BaseMapper<RolePermissionPo> {

    int batchInsertPermissions(@Param("list") List<RolePermissionPo> list);

    int removeRolePermission(@Param("vo") RolePermissionVo rolePermissionVo);

    List<PermissionVo> queryPermissionByRoleId(Long roleId);

    List<UserRoleVo> queryRoleListByUserId(Long userId);
}


package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录信息")
public class UserInfoVo extends UserVo implements Serializable {
    private static final long serialVersionUID = -6927633583276994707L;
    private String token;
    private RoleVo curRole;
    private Set<String> curPermissionSet;
    private List<UserRoleVo> validRoleList;
}

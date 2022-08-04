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
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = -6927633583276994707L;
    private String token;
    private UserVo curUser;
    private RoleVo curRole;
    private List<String> curPermissionList;
    private List<UserRoleVo> validRoleList;
}

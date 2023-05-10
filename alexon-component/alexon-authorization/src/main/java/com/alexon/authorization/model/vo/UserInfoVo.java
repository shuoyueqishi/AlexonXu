package com.alexon.authorization.model.vo;

import com.alexon.authorization.model.response.WechatLoginRes;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录信息")
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = -6927633583276994707L;
    private String token;
    private UserVo curUser;
    private RoleVo curRole;
    private WechatLoginRes wechatLoginRes;
    private List<String> curPermissionList;
    private List<UserRoleVo> validRoleList;
}

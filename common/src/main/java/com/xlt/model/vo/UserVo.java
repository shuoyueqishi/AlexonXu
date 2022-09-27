package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户信息")
public class UserVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = -7751119896864993134L;
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("用户别名")
    private String nickName;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("电话")
    private String telephone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("头像docNo")
    private String headImg;
    @ApiModelProperty("默认角色")
    private Long defaultRole;
}

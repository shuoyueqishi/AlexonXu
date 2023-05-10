package com.alexon.authorization.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq extends WechatLoginReq {

    @ApiModelProperty("用户登录类型，参考枚举类LoginEnum，可传值：wechat, wxMiniProgram、normal、email、telephone")
    private String loginType;

    @ApiModelProperty("微信用户的唯一标识")
    private String openId;

    @ApiModelProperty("微信用户在开放平台的唯一标识符")
    private String unionId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("电话")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String email;
}

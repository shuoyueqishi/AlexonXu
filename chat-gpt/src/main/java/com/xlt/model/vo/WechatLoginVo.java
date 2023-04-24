package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("小程序登录VO")
public class WechatLoginVo {
    @ApiModelProperty("小程序 appId")
    private String appid;

    @ApiModelProperty("小程序 appSecret")
    private String secret;

    @ApiModelProperty("登录时获取的 code，可通过wx.login获取")
    private String js_code;

    @ApiModelProperty("授权类型，此处只需填写 authorization_code")
    @Builder.Default
    private String grant_type = "authorization_code";
}

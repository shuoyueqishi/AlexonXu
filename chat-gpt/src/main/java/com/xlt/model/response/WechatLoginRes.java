package com.xlt.model.response;


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
@ApiModel("小程序登录API返回")
public class WechatLoginRes {
    @ApiModelProperty("会话密钥")
    private String session_key;

    @ApiModelProperty("用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。")
    private String unionid;

    @ApiModelProperty("错误信息")
    private String errmsg;

    @ApiModelProperty("用户唯一标识")
    private String openid;

    @ApiModelProperty("错误码")
    private Long errcode;
}

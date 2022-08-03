package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("权限信息")
public class PermissionVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 5478647065231146043L;
    @ApiModelProperty("permissionId")
    private Long permissionId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("操作码")
    private String operateCode;

    @ApiModelProperty("操作描述")
    private String operateDesc;

    @ApiModelProperty("接口路径")
    private String path;

    @ApiModelProperty("'HTTP请求方式'")
    private String httpMethod;

    @ApiModelProperty("'方法全路径名'")
    private String methodName;

    @ApiModelProperty("租户,微服务名称")
    private String tenant;
}

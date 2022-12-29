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
@ApiModel("操作日志信息")
public class OperationLogVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1142825251639390814L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("操作模块")
    private String operateModule;

    @ApiModelProperty("操作类型")
    private String operateType;

    @ApiModelProperty("操作描述")
    private String operateDesc;

    @ApiModelProperty("操作URL")
    private String url;

    @ApiModelProperty("请求参数")
    private String request;

    @ApiModelProperty("响应参数")
    private String response;

    @ApiModelProperty("操作人IP")
    private String userIp;
}

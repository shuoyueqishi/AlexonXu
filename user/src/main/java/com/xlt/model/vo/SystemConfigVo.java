package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统配置表")
public class SystemConfigVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 1142825251639390814L;

    @ApiModelProperty("configId")
    private Long configId;

    @ApiModelProperty("配置编码")
    private String configCode;

    @ApiModelProperty("配置类型，1：字符串、2：json")
    @Max(2)
    @Min(1)
    private Integer configType;

    @ApiModelProperty("配置类型，1：字符串、2：json")
    private String configTypeDesc;

    @ApiModelProperty("值")
    private Object value;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("删除标识，0：未删除，1：已删除")
    private Integer deleted;

    @ApiModelProperty("删除标识，0：未删除，1：已删除")
    private String deletedDesc;

}

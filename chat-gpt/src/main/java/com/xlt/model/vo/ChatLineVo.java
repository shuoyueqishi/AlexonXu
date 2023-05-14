package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
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
@ApiModel("chat行信息")
public class ChatLineVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 5237204071607082641L;

    @ApiModelProperty("line id")
    private Long lineId;

    @ApiModelProperty(value="head id")
    private Long headId;

    @ApiModelProperty("对话角色")
    private String chatRole;

    @ApiModelProperty("对话内容")
    private String chatContent;

}

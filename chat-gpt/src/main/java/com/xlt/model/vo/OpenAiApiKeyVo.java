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
@ApiModel("OpenAI API Key信息")
public class OpenAiApiKeyVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 3229491664349613256L;

    @ApiModelProperty(value="id")
    private Long id;

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("apiKey")
    private String apiKey;

}

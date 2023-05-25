package com.xlt.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("OpenAI API Key信息请求")
public class ApiKeyRequest {

    @ApiModelProperty("user id")
    private Long userId;

    @ApiModelProperty("apiKey")
    private String apiKey;
}

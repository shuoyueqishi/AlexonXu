package com.xlt.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("OpenAI 访问信息")
public class AccessInfoVo {

    private Long userId;
    private String apiKey;
    private Integer credit;
}

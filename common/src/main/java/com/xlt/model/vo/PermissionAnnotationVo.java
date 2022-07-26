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
@ApiModel("权限信息")
public class PermissionAnnotationVo {
    private String resourceName;
    private String operateCode;
    private String operateDesc;
}

package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("权限信息")
public class PermissionVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 5478647065231146043L;
    private Long permissionId;

    private String apiOperation;

    private String path;

    private String tenant;
}

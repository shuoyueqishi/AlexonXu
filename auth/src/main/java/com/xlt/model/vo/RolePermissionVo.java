package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("角色授权信息")
public class RolePermissionVo  extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1112319886276970929L;

    private Long roleId;

    private List<Long> permissionList;
}

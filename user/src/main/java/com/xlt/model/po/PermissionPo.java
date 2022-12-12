package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("permission_t")
public class PermissionPo extends BasePo implements Serializable {

    private static final long serialVersionUID = -1323424357091886666L;

    @TableId(value = "permission_id",type = IdType.AUTO)
    private Long permissionId;

    @TableField("resource_name")
    private String resourceName;

    @TableField("operate_code")
    private String operateCode;

    @TableField("operate_desc")
    private String operateDesc;

    @TableField("path")
    private String path;

    @TableField("http_method")
    private String httpMethod;

    @TableField("method_name")
    private String methodName;

    @TableField("tenant")
    private String tenant;
}

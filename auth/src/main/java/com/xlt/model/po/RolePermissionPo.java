package com.xlt.model.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="role_permission_t")
public class RolePermissionPo extends TkBasePo implements Serializable {
    private static final long serialVersionUID = -5021749017548597914L;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;
}

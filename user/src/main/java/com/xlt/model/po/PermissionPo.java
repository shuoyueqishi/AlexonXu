package com.xlt.model.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="permission_t")
public class PermissionPo extends TkBasePo implements Serializable {

    private static final long serialVersionUID = -1323424357091886666L;
    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "operate_code")
    private String operateCode;

    @Column(name = "operate_desc")
    private String operateDesc;

    @Column(name = "tenant")
    private String tenant;
}
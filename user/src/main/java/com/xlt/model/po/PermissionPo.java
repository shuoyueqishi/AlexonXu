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

    @Column(name = "api_operation")
    private String apiOperation;

    @Column(name = "path")
    private String path;

    @Column(name = "tenant")
    private String tenant;
}
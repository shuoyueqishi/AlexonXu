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
@Table(name="role_t")
public class RolePo extends TkBasePo implements Serializable {

    private static final long serialVersionUID = 7249862574164740075L;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_desc")
    private String roleDesc;

    @Column(name = "tenant")
    private String tenant;
}
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
@Table(name="user_t")
public class UserPo extends TkBasePo implements Serializable {
    private static final long serialVersionUID = -6547364084413382629L;
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    @Column(name = "default_role")
    private Long defaultRole;

}
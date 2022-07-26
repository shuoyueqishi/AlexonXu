package com.xlt.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = -7751119896864993134L;

    private Long userId;

    private String nickName;

    private String name;

    private String password;

    private String telephone;

    private String email;

    private String currentRole;
}

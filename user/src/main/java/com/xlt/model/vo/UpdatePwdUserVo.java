package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("更新用户密码VO")
public class UpdatePwdUserVo extends UserVo implements Serializable {
    private static final long serialVersionUID = 8757869345293040294L;
    private String newPassword;
}

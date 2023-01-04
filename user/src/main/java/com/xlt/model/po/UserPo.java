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
@TableName("user_t")
public class UserPo extends BasePo implements Serializable {
    private static final long serialVersionUID = -6547364084413382629L;

    @TableId(value = "user_id",type = IdType.AUTO)
    private Long userId;

    @TableField("nick_name")
    private String nickName;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("telephone")
    private String telephone;

    @TableField("email")
    private String email;

    @TableField("head_img")
    private String headImg;

    @TableField("default_role")
    private Long defaultRole;

}

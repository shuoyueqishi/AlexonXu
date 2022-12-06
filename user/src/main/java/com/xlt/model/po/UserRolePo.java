package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_role_t")
public class UserRolePo extends BasePo implements Serializable {

    private static final long serialVersionUID = -3455587923768658816L;

    @TableField("user_id")
    private Long userId;

    @TableField("role_id")
    private Long roleId;

    @TableField("start_time")
    private Date startTime;

    @TableField("end_time")
    private Date endTime;
}

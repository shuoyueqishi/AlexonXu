package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_t")
public class UserPo {
    @TableId("id")
    private Long id;

    @TableField("number")
    private String number;

    @TableField("name")
    private String name;

    @TableField("position")
    private String position;
}

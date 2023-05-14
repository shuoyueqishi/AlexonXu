package com.xlt.model.po;

import com.alexon.model.po.BasePo;
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
@TableName("chat_head_t")
public class ChatHeadPo extends BasePo implements Serializable {

    private static final long serialVersionUID = -768300902862033825L;

    @TableId(value="head_id", type = IdType.AUTO)
    private Long headId;

    @TableField("user_id")
    private String userId;

    @TableField("chat_name")
    private String chatName;
}

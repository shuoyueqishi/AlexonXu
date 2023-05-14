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
@TableName("chat_line_t")
public class ChatLinePo extends BasePo implements Serializable {

    private static final long serialVersionUID = -7972996606102194661L;

    @TableId(value="line_id", type = IdType.AUTO)
    private Long lineId;

    @TableField("head_id")
    private Long headId;

    @TableField("chat_role")
    private String chatRole;

    @TableField("chat_content")
    private String chatContent;
}

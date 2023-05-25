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
@TableName("open_api_key_t")
public class OpenAiApiKeyPo extends BasePo implements Serializable {
    private static final long serialVersionUID = -5365380644562735841L;

    @TableId(value="id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("api_key")
    private String apiKey;
}

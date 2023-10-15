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
@TableName("system_config_t")
public class SystemConfigPo extends BasePo implements Serializable {

    private static final long serialVersionUID = -5024356078512630116L;

    @TableId(value="config_id", type = IdType.AUTO)
    private Long configId;

    @TableField("config_code")
    private String configCode;

    @TableField("config_type")
    private String configType;

    @TableField("value")
    private Object value;

    @TableField("description")
    private String description;

    @TableField("deleted")
    private Integer deleted;
}

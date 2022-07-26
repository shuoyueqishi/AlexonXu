package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("delivery_t")
public class DeliveryPo extends BasePo implements Serializable {
    private static final long serialVersionUID = 7898138260207339633L;

    @TableId("delivery_id")
    private Long delivery_id;

    @TableField("delivery_type")
    private String delivery_type;

    @TableField("delivery_no")
    private String delivery_no;

    @TableField("status")
    private Integer status;
}

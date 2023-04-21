package com.alexon.authorization.model.po;

import com.alexon.model.po.BasePo;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("product_t")
public class ProductPo extends BasePo implements Serializable {
    private static final long serialVersionUID = 2660677350391367339L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("product_code")
    private String productCode;

    @TableField("product_name")
    private String productName;

    @TableField("price")
    private BigDecimal price;

    @TableField("description")
    private String description;

    @TableField("remark")
    private String remark;

    @TableField("valid_flag")
    private Integer validFlag;
}

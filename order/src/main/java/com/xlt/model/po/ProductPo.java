package com.xlt.model.po;

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
    private static final long serialVersionUID = 7515750184025146732L;

    @TableId(value="product_id",type = IdType.AUTO)
    private Long productId;

    @TableField("product_code")
    private String productCode;

    @TableField("product_name")
    private String productName;

    @TableField("product_desc")
    private String productDesc;

    @TableField("price")
    private BigDecimal price;

    @TableField("stock")
    private Long stock;

    @TableField("status")
    private Integer status;

    @TableField("deleted")
    private Integer deleted;
}

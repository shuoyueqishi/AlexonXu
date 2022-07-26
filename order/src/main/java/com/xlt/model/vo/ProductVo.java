package com.xlt.model.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1906603758352569839L;

    private Long productId;

    private String productCode;

    private String productName;

    private String productDesc;

    private BigDecimal price;

    private Long stock;

    private Integer status;

    private String statusDesc;

    private Integer deleted;

}

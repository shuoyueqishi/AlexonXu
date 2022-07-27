package com.xlt.model.po;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="product_t")
public class ProductPo extends TkBasePo implements Serializable {
    private static final long serialVersionUID = 2660677350391367339L;
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="product_code")
    private String productCode;

    @Column(name="product_name")
    private String productName;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="description")
    private String description;

    @Column(name="remark")
    private String remark;

    @Column(name="valid_flag")
    private Integer validFlag;
}

package com.xlt.model.vo;


import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCommodityVo extends BaseVo {
    private static final long serialVersionUID = -4238561233305284729L;
    private Long id;
    private Long orderId;
    private Long skuId;
    private Long quantity;
}

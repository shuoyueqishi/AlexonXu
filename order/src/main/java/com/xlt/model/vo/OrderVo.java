package com.xlt.model.vo;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = 1906603758352569839L;
    private Long orderId;

    private String orderNo;

    private Long userId;

    private String userStr;

    private Long receiveAddressId;

    private BigDecimal amount;

    private Integer status;

    private String statusDesc;

    private Integer deleted;

    private List<OrderProductDeliveryVo> productList;

}

package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
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

    private BigDecimal orderAmount;

    private BigDecimal deliveryAmount;

    private BigDecimal totalAmount;

    private Long receiverId;

    private String receiverStr;

    private Integer status;

    private String statusStr;

    private Integer deleted;

    private List<OrderCommodityVo> commodityList;

}

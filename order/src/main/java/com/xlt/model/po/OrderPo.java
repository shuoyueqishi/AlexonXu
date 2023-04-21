package com.xlt.model.po;

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
@TableName("order_t")
public class OrderPo extends BasePo implements Serializable {
    private static final long serialVersionUID = 1337475900522675773L;

    @TableId(value = "order_id",type = IdType.AUTO)
    private Long orderId;

    @TableField("order_no")
    private String orderNo;

    @TableField("user_id")
    private Long userId;

    @TableField("receiver_id")
    private Long receiverId;

    @TableField("order_amount")
    private BigDecimal orderAmount;

    @TableField("delivery_amount")
    private BigDecimal deliveryAmount;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("status")
    private Integer status;

    @TableField("deleted")
    private Integer deleted;
}

package com.xlt.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverInfoVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = -2982268944660836821L;

    @ApiModelProperty("receiver_id主键")
    private Long receiverId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("收件人姓名")
    private String receiverName;

    @ApiModelProperty("收件人电话")
    private String receiverTelephone;

    @ApiModelProperty("收件人地址")
    private String receiverAddress;

    @ApiModelProperty("是否默认地址，0：否，1：是")
    private Integer defaultFlag;

}

package com.xlt.model.po;

import com.alexon.model.po.BasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("receiver_info_t")
public class ReceiverInfoPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 2062023405087330996L;

    @TableId(value = "receiver_id",type = IdType.AUTO)
    private Long receiverId;

    @TableField("user_id")
    private Long userId;

    @TableField("receiver_name")
    private String receiverName;

    @TableField("receiver_telephone")
    private String receiverTelephone;

    @TableField("receiver_address")
    private String receiverAddress;

    @TableField("default_flag")
    private Integer defaultFlag;
}

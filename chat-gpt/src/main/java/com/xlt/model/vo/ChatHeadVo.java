package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("chat头信息")
public class ChatHeadVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 3229491664349613256L;

    @ApiModelProperty(value="head id")
    private Long headId;

    @ApiModelProperty("user id")
    private String userId;

    @ApiModelProperty("chat名称")
    private String chatName;

}

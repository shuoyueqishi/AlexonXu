package com.xlt.model.request;

import com.xlt.model.vo.ChatHeadVo;
import com.xlt.model.vo.ChatLineVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("新增聊天信息请求")
public class ChatContentRequest extends ChatHeadVo implements Serializable {

    private static final long serialVersionUID = -8462141516167016321L;

    @ApiModelProperty("聊天详情信息列表")
    private List<ChatLineVo> chatLineVoList;
}

package com.xlt.model.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("删除聊天信息请求")
public class ChatDeleteRequest implements Serializable {
    private static final long serialVersionUID = 4149914931373930515L;

    @NotEmpty
    private List<Long> headIdList;
}

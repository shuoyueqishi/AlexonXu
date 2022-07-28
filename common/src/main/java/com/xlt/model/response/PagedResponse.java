package com.xlt.model.response;

import com.xlt.model.vo.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页数据返回")
public class PagedResponse<T> extends BasicResponse implements Serializable {

    private static final long serialVersionUID = 6868711578066939844L;

    @ApiModelProperty("数据")
    private T data;
    @ApiModelProperty("分页")
    private PageVo page;
}

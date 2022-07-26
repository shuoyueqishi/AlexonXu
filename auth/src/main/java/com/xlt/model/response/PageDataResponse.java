package com.xlt.model.response;

import com.xlt.model.vo.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel("通用返回")
public class PageDataResponse<T> extends BasicResponse implements Serializable {

    private static final long serialVersionUID = -1888884229708776345L;

    @ApiModelProperty("数据")
    private List<T> data;
    @ApiModelProperty("分页")
    private PageVo pageVo;
}

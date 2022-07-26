package com.xlt.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("带数据的返回")
public class DataResponse<T> extends BasicResponse implements Serializable {
    private static final long serialVersionUID = -5462484488629260684L;
    @ApiModelProperty("数据")
    private T data;
}

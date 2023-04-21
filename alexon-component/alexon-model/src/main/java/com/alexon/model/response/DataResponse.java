package com.alexon.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
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

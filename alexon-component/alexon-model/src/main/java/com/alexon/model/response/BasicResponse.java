package com.alexon.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("基础返回")
public class BasicResponse implements Serializable {
    private static final long serialVersionUID = 3133901045675557228L;
    @ApiModelProperty("返回具体信息")
    private String message="success to execute";
    @ApiModelProperty("返回码")
    private String code="200";
    @ApiModelProperty("返回状态")
    private boolean success=true;
    //自定义返回结果的构造方法
    public BasicResponse(Boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.message = msg;
    }

    public BasicResponse(String msg) {
        this.message=msg;
    }

    //其他异常处理方法返回的结果
    public static BasicResponse errorWithMsg(String errMsg){
        BasicResponse result = new BasicResponse();
        result.setMessage(errMsg);
        result.setCode("500");
        result.setSuccess(false);
        return result;
    }
}

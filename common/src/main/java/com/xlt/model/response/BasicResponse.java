package com.xlt.model.response;

import com.xlt.exception.CommonException;
import com.xlt.exception.ErrorEnum;
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
    //自定义异常返回的结果
    public static BasicResponse commonError(CommonException commonException){
        BasicResponse result = new BasicResponse();
        result.setSuccess(false);
        result.setCode(commonException.getErrorCode());
        result.setMessage(commonException.getErrorMsg());
        return result;
    }
    //其他异常处理方法返回的结果
    public static BasicResponse otherError(ErrorEnum errorEnum){
        BasicResponse result = new BasicResponse();
        result.setMessage(errorEnum.getErrorMsg());
        result.setCode(errorEnum.getErrorCode());
        result.setSuccess(false);
        return result;
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

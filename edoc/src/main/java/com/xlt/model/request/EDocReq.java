package com.xlt.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文档信息查询")
public class EDocReq implements Serializable {
    private static final long serialVersionUID = -1436294314866064715L;

    @ApiModelProperty("文档编码")
    private String docNo;

    @ApiModelProperty("文档名称")
    private String docName;

    @ApiModelProperty("文档类型")
    private String docType;

    @ApiModelProperty("文档是否已删除，0：否，1：是")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间起")
    private Date creationDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间止")
    private Date creationDateEnd;

}

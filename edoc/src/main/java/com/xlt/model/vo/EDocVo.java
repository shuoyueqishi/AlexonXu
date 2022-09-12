package com.xlt.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文档信息")
public class EDocVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = -8608883021495531033L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("文档编码")
    private String docNo;

    @ApiModelProperty("文档名称")
    private String docName;

    @ApiModelProperty("文档类型")
    private String docType;

    @ApiModelProperty("文档大小，默认单位KB")
    private Long docSize;

    @ApiModelProperty("文档下载链接")
    private String downloadUrl;

    @ApiModelProperty("文档是否已删除，0：否，1：是")
    private Integer deleted;
}

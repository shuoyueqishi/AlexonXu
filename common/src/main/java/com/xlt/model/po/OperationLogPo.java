package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("operation_log_t")
public class OperationLogPo implements Serializable {

    private static final long serialVersionUID = 7386866274177160395L;

    @TableId(value="id", type = IdType.AUTO)
    private Long id;

    @TableField("operate_module")
    private String operateModule;

    @TableField("operate_type")
    private String operateType;

    @TableField("operate_desc")
    private String operateDesc;

    @TableField("url")
    private String url;

    @TableField("request")
    private String request;

    @TableField("response")
    private String response;

    @TableField("user_ip")
    private String userIp;

    @TableField("create_by")
    private Long createBy;

    @TableField("creation_date")
    private Date creationDate;

}

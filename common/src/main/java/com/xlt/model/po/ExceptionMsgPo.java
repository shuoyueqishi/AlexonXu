package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("exception_log_t")
public class ExceptionMsgPo {
    @TableId(value="id",type = IdType.AUTO)
    private Long id;

    @TableField("msg")
    private String msg;

    @TableField("stack_trace")
    private String stackTrace;

    @TableField("create_by")
    protected Long createBy;

    @TableField("creation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
}

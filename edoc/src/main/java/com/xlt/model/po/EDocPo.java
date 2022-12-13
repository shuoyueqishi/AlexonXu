package com.xlt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("edoc_t")
public class EDocPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 5243137674477511942L;

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @TableField("doc_no")
    private String docNo;

    @TableField("doc_name")
    private String docName;

    @TableField("file_name")
    private String fileName;

    @TableField("doc_type")
    private String docType;

    @TableField("doc_size")
    private Long docSize;

    @TableField("download_url")
    private String downloadUrl;

    @TableField("deleted")
    private Integer deleted;
}

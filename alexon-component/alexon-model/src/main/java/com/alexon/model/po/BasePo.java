package com.alexon.model.po;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePo implements Serializable {
    private static final long serialVersionUID = -1113179217211499942L;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("creation_date")
    protected Date creationDate;

    /**
     * 创建人
     */
    @TableField("create_by")
    protected Long createBy;

    /**
     * 最近更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_update_date")
    protected Date lastUpdateDate;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    protected Long lastUpdateBy;

    /**
     * 设置创建人和创建时间
     *
     * @param basePoObj basePoObj的子类对象
     * @param <T>           对象类型
     */
    public <T extends BasePo> void setUserAndTime(T basePoObj) {
        this.createBy = basePoObj.getCreateBy();
        this.creationDate = basePoObj.getCreationDate();
        this.lastUpdateBy = basePoObj.getLastUpdateBy();
        this.lastUpdateDate = basePoObj.getLastUpdateDate();
    }
}

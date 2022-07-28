package com.xlt.model.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

@Data
public class TkBasePo {
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "creation_date")
    protected Date creationDate;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    protected Long createBy;

    /**
     * 最近更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_update_date")
    protected Date lastUpdateDate;

    /**
     * 更新人
     */
    @Column(name = "last_update_by")
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

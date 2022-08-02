package com.xlt.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo implements Serializable {
    private static final long serialVersionUID = 4010689365258880213L;

    private Long createBy;

    private String createByStr;

    private Date creationDate;

    private Long lastUpdateBy;

    private String lastUpdateByStr;

    private Date LastUpdateDate;
}

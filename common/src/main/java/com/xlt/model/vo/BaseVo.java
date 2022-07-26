package com.xlt.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo implements Serializable {
    private static final long serialVersionUID = 4010689365258880213L;

    private Long createBy;

    private String createByStr;

    private Long creationDate;

    private String creationDateStr;

    private Long lastUpdateBy;

    private String lastUpdateByStr;

    private Long LastUpdateDate;

    private String LastUpdateDateStr;
}

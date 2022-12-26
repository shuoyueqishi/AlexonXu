package com.xlt.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogQueryVo extends OperationLogVo implements Serializable {
    private static final long serialVersionUID = -5762829769525902419L;

    private Date creationDateStart;

    private Date creationDateEnd;
}

package com.alexon.authorization.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogQueryVo extends OperationLogVo implements Serializable {
    private static final long serialVersionUID = -5762829769525902419L;

    private String creationDateStart;

    private String creationDateEnd;
}

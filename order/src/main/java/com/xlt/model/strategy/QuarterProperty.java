package com.xlt.model.strategy;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QuarterProperty implements Serializable {
    private static final long serialVersionUID = -807856576312746509L;

    private String lowerLimit;

    private String upperLimit;

    private Integer interval;

    private String pattern;
}

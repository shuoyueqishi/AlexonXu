package com.xlt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WenxinFunction implements Serializable {
    private static final long serialVersionUID = 6153514952142189940L;
    private String name;
    private String description;
    private Object parameters;
    private Object responses;
    private List<WenxinExample> examples;
}

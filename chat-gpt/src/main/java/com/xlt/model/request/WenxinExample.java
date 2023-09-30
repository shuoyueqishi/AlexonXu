package com.xlt.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WenxinExample implements Serializable {
    private static final long serialVersionUID = 3371493812464734553L;

    private String role;
    private String name;
    private String content;
    private WenxinFunctionCall function_call;
}

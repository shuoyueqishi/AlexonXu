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
public class WenxinFunctionCall implements Serializable {
    private static final long serialVersionUID = 3515347332414644301L;
    private String name;
    private String thoughts;
    private String arguments;
}

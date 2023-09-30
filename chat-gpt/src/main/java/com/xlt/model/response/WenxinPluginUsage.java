package com.xlt.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WenxinPluginUsage implements Serializable {

    private static final long serialVersionUID = 6049749480605277930L;
    private String name;
    private Integer parse_tokens;
    private Integer abstract_tokens;
    private Integer search_tokens;
    private Integer total_tokens;
}

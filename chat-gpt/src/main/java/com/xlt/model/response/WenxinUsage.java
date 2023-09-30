package com.xlt.model.response;

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
public class WenxinUsage implements Serializable {

    private static final long serialVersionUID = 2476055604832013L;

    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
    private List<WenxinPluginUsage> plugins;
}

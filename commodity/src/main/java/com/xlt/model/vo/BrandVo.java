package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandVo extends BaseVo implements Serializable {
    private static final long serialVersionUID = -9192750611849287092L;

    private Long brandId;

    private String name;

    private String image;

    private String initial;
}

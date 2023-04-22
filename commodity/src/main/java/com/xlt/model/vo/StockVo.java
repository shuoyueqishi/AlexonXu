package com.xlt.model.vo;


import com.alexon.model.vo.BaseVo;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockVo extends BaseVo implements Serializable {

    private static final long serialVersionUID = 2081531758354994745L;

    private Long id;

    private Long skuId;

    private Long quantity;

}

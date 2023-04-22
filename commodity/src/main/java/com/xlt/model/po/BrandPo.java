package com.xlt.model.po;

import com.alexon.model.po.BasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("brand_t")
public class BrandPo extends BasePo implements Serializable {

    private static final long serialVersionUID = -957727940372645265L;

    @TableId(value="brand_id",type = IdType.AUTO)
    private Long brandId;

    @TableField("name")
    private String name;

    @TableField("image")
    private String image;

    @TableField("initial")
    private String initial;

}

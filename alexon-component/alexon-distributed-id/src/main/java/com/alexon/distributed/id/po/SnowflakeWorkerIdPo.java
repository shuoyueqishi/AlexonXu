package com.alexon.distributed.id.po;

import com.alexon.model.po.BasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("snowflake_worker_id_t")
public class SnowflakeWorkerIdPo extends BasePo {
    private static final long serialVersionUID = -3719774724079265192L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
}

package com.xlt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.PermissionPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper extends BaseMapper<PermissionPo> {

    int batchInsert(@Param("list") List<PermissionPo> list);

    List<PermissionPo> queryPermissionsByPoint(@Param("list") List<String> list);
}

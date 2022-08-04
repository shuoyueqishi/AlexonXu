package com.xlt.mapper;


import com.xlt.model.po.PermissionPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface PermissionMapper extends Mapper<PermissionPo> {

    int batchInsert(@Param("list") List<PermissionPo> list);

    List<PermissionPo> queryPermissionsByPoint(@Param("list") List<String> list);
}

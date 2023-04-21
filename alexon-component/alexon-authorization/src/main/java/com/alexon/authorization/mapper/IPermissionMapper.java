package com.alexon.authorization.mapper;


import com.alexon.authorization.model.po.PermissionPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IPermissionMapper extends BaseMapper<PermissionPo> {

    int batchInsert(@Param("list") List<PermissionPo> list);

    List<PermissionPo> queryPermissionsByPoint(@Param("list") List<String> list);
}

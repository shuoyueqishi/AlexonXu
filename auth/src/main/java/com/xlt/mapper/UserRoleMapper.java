package com.xlt.mapper;

import com.xlt.model.po.UserRolePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Component
public interface UserRoleMapper extends Mapper<UserRolePo> {

    int batchInsert(@Param("list")List<UserRolePo> list);

}

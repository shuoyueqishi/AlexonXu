package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.UserRolePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface IUserRoleMapper extends BaseMapper<UserRolePo> {

    int batchInsert(@Param("list")List<UserRolePo> list);

}

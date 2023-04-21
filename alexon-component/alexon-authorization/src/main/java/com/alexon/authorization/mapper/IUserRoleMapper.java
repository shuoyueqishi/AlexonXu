package com.alexon.authorization.mapper;

import com.alexon.authorization.model.po.UserRolePo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface IUserRoleMapper extends BaseMapper<UserRolePo> {

    int batchInsert(@Param("list")List<UserRolePo> list);

}

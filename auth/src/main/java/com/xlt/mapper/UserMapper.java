package com.xlt.mapper;


import com.xlt.model.po.UserPo;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;


@Component
public interface UserMapper extends Mapper<UserPo> {
}
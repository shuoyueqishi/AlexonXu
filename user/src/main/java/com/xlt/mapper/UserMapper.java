package com.xlt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.UserPo;
import org.springframework.stereotype.Component;


@Component
public interface UserMapper extends BaseMapper<UserPo> {
}

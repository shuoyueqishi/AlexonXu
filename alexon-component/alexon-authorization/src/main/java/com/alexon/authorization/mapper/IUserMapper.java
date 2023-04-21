package com.alexon.authorization.mapper;


import com.alexon.authorization.model.po.UserPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;


@Component
public interface IUserMapper extends BaseMapper<UserPo> {
}

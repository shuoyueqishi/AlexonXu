package com.alexon.authorization.model.convertor;

import com.alexon.authorization.model.po.UserPo;
import com.alexon.authorization.model.vo.UserVo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true) )
public interface UserConvertor {
    UserConvertor INSTANCE = Mappers.getMapper(UserConvertor.class);

    UserVo toUserVo(UserPo userPo);

    UserPo toUserPo(UserVo userVo);
}

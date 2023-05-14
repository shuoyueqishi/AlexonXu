package com.xlt.model.conventors;

import com.xlt.model.po.ChatHeadPo;
import com.xlt.model.request.ChatContentRequest;
import com.xlt.model.vo.ChatHeadVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatHeadConvertor {
    ChatHeadConvertor INSTANCE = Mappers.getMapper(ChatHeadConvertor.class);

    ChatHeadVo toChatHeadVo(ChatHeadPo chatHeadPo);

    List<ChatHeadVo> toChatHeadVoList(List<ChatHeadPo> chatHeadPoList);

    ChatHeadPo toChatHeadPo(ChatHeadVo chatHeadVo);

    ChatHeadPo toChatHeadPo(ChatContentRequest chatContentRequest);
}

package com.xlt.model.conventors;

import com.xlt.model.po.ChatLinePo;
import com.xlt.model.vo.ChatLineVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ChatLineConvertor {
    ChatLineConvertor INSTANCE = Mappers.getMapper(ChatLineConvertor.class);

    ChatLineVo toChatLineVo(ChatLinePo chatLinePo);

    List<ChatLineVo> toChatLineVoList(List<ChatLinePo> chatLinePoList);

    ChatLinePo toChatLinePo(ChatLineVo chatLineVo);
}

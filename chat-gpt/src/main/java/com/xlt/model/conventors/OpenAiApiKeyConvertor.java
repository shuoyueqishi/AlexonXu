package com.xlt.model.conventors;

import com.xlt.model.po.ChatHeadPo;
import com.xlt.model.po.OpenAiApiKeyPo;
import com.xlt.model.request.ChatContentRequest;
import com.xlt.model.vo.ChatHeadVo;
import com.xlt.model.vo.OpenAiApiKeyVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OpenAiApiKeyConvertor {
    OpenAiApiKeyConvertor INSTANCE = Mappers.getMapper(OpenAiApiKeyConvertor.class);

    OpenAiApiKeyVo toOpenAiApiKeyVo(OpenAiApiKeyPo openAiApiKeyPo);

    List<OpenAiApiKeyVo> toOpenAiApiKeyVoList(List<OpenAiApiKeyPo> list);

    OpenAiApiKeyPo toOpenAiApiKeyPo(OpenAiApiKeyPo openAiApiKeyVo);
}

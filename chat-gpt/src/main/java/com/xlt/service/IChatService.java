package com.xlt.service;

import com.xlt.model.request.ChatContentRequest;
import com.xlt.model.vo.ChatHeadVo;
import com.xlt.model.vo.ChatLineVo;

import java.util.List;

public interface IChatService {

    Long addChatContent(ChatContentRequest request);

    List<ChatHeadVo> queryChatHeadList(ChatHeadVo chatHeadVo);

    List<ChatLineVo> queryChatLineList(ChatLineVo chatLineVo);

    void deleteChat(List<Long> headIdList);

    void updateChatName(ChatHeadVo chatHeadVo);
}

package com.xlt.service.impl;

import com.alexon.authorization.context.UserContext;
import com.alexon.authorization.utils.PoUtil;
import com.alexon.exception.utils.AssertUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xlt.mapper.ChatHeadMapper;
import com.xlt.mapper.ChatLineMapper;
import com.xlt.model.conventors.ChatHeadConvertor;
import com.xlt.model.conventors.ChatLineConvertor;
import com.xlt.model.po.ChatHeadPo;
import com.xlt.model.po.ChatLinePo;
import com.xlt.model.request.ChatContentRequest;
import com.xlt.model.vo.ChatHeadVo;
import com.xlt.model.vo.ChatLineVo;
import com.xlt.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ChatService implements IChatService {

    @Resource
    private ChatHeadMapper chatHeadMapper;

    @Resource
    private ChatLineMapper chatLineMapper;

    @Override
    @Transactional
    public Long addChatContent(ChatContentRequest request) {
        log.info("add chat content request:{}", request);
        AssertUtil.isNull(request, "request can't be empty");
        AssertUtil.isNull(request.getUserId(), "userId can't be empty");
        AssertUtil.isCollectionEmpty(request.getChatLineVoList(), "chatLineList can't be empty");

        if(Objects.nonNull(request.getUserId())) {
            log.info("headId:{} exited, delete it and it's detail first",request.getHeadId());
            QueryWrapper<ChatLinePo> lineWrapper = new QueryWrapper<>();
            lineWrapper.eq("head_id",request.getHeadId());
            chatLineMapper.delete(lineWrapper);
            chatHeadMapper.deleteById(request.getHeadId());
        }
        if (StringUtils.isEmpty(request.getChatName())) {
            String firstChatContent = request.getChatLineVoList().get(0).getChatContent();
            AssertUtil.isStringEmpty(firstChatContent, "first chat line content is empty");
            String chatName = firstChatContent.length() > 16 ? firstChatContent.substring(0, 16) : firstChatContent;
            request.setChatName(chatName);
        }
        ChatHeadPo chatHeadPo = ChatHeadConvertor.INSTANCE.toChatHeadPo(request);
        PoUtil.buildCreateUserInfo(chatHeadPo);
        chatHeadMapper.addChatHead(chatHeadPo);
        List<ChatLinePo> chatLinePoList = new ArrayList<>();
        AssertUtil.isNull(chatHeadPo.getHeadId(), "empty headId returned after insert");
        for (ChatLineVo chatLineVo : request.getChatLineVoList()) {
            chatLineVo.setHeadId(chatHeadPo.getHeadId());
            ChatLinePo chatLinePo = ChatLineConvertor.INSTANCE.toChatLinePo(chatLineVo);
            PoUtil.buildCreateUserInfo(chatLinePo);
            chatLinePoList.add(chatLinePo);
        }
        chatLineMapper.addBatchChatLines(chatLinePoList);
        log.info("success to add chat content");
        return chatHeadPo.getHeadId();
    }

    @Override
    public List<ChatHeadVo> queryChatHeadList(ChatHeadVo chatHeadVo) {
        log.info("queryChatHeadList input:{}", chatHeadVo);
        QueryWrapper<ChatHeadPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", UserContext.getUserId());
        queryWrapper.likeRight(StringUtils.isNotEmpty(chatHeadVo.getChatName()), "chat_name", chatHeadVo.getChatName());
        queryWrapper.orderByDesc("last_update_date");
        List<ChatHeadPo> chatHeadPos = chatHeadMapper.selectList(queryWrapper);
        return ChatHeadConvertor.INSTANCE.toChatHeadVoList(chatHeadPos);
    }

    @Override
    public List<ChatLineVo> queryChatLineList(ChatLineVo chatLineVo) {
        log.info("queryChatLineList input:{}", chatLineVo);
        AssertUtil.isNull(chatLineVo.getHeadId(),"headId can't be empty");
        QueryWrapper<ChatLinePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("head_id", chatLineVo.getHeadId());
        queryWrapper.eq(StringUtils.isNotEmpty(chatLineVo.getChatRole()), "chat_role", chatLineVo.getChatRole());
        queryWrapper.orderByAsc("last_update_date");
        List<ChatLinePo> chatLinePoList = chatLineMapper.selectList(queryWrapper);
        return ChatLineConvertor.INSTANCE.toChatLineVoList(chatLinePoList);
    }

    @Override
    public void deleteChat(List<Long> headIdList) {
        log.info("deleteChat input={}",headIdList);
        AssertUtil.isCollectionEmpty(headIdList,"headIdList can't be empty");
        chatHeadMapper.deleteBatchIds(headIdList);
        log.info("delete chat head success");
        QueryWrapper<ChatLinePo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("head_id",headIdList);
        chatLineMapper.delete(queryWrapper);
        log.info("success to delete chat lines");
    }

    @Override
    public void updateChatName(ChatHeadVo chatHeadVo) {
        log.info("updateChatName for:{}",chatHeadVo);
        AssertUtil.isNull(chatHeadVo.getHeadId(),"headId can't be empty");
        AssertUtil.isStringEmpty(chatHeadVo.getChatName(),"new chat name can't be empty");
        QueryWrapper<ChatHeadPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("head_id",chatHeadVo.getHeadId());
        ChatHeadPo existed = chatHeadMapper.selectOne(queryWrapper);
        AssertUtil.isNull(existed,"chat not exist");
        if(chatHeadVo.getChatName().equals(existed.getChatName())) {
            log.info("chat name is as same as it in database");
            return;
        }
        existed.setChatName(chatHeadVo.getChatName());
        PoUtil.buildUpdateUserInfo(existed);
        chatHeadMapper.updateById(existed);
        log.info("success to update chat name");
    }
}
